package com.jhc.chathub.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.jhc.chathub.common.constants.SystemConstant;
import com.jhc.chathub.common.resp.WSResponse;
import com.jhc.chathub.pojo.dto.user.UserDTO;
import com.jhc.chathub.pojo.vo.ShowMsgVO;
import com.jhc.chathub.service.IMessageService;
import com.jhc.chathub.service.IUserService;
import com.jhc.chathub.service.IWebSocketService;
import com.jhc.chathub.websocket.NettyUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class WebSocketServiceImpl implements IWebSocketService {
    @Resource
    private IUserService userService;

    @Resource
    private IMessageService messageService;

    // 房间号 -> channels
    // CopyOnWriteArrayList: 读写分离的list, 读不加锁, 写加锁
    private static final ConcurrentHashMap<Long, CopyOnWriteArrayList<Channel>> ROOM_CHANNEL_MAP = new ConcurrentHashMap<>();

    // 线程池执行推送消息
    private static final ExecutorService SEND_MSG_TASK_EXECUTOR = Executors.newFixedThreadPool(10);

    @Override
    public void authorize(Channel channel) {
        // 1.得到channel中的token并进行认证
        String token = NettyUtil.getAttr(channel, NettyUtil.TOKEN);
        if (StrUtil.isBlank(token)) {
            sendMsgByChannel(channel, WSResponse.build(SystemConstant.WS_HEAD_SHAKE_FAIL_MSG, "认证失败"));
            channel.close();
            return;
        }
        UserDTO user = userService.getUserByToken(token);
        if (user == null) {
            sendMsgByChannel(channel, WSResponse.build(SystemConstant.WS_HEAD_SHAKE_FAIL_MSG, "认证失败"));
            channel.close();
            return;
        }

        // 2.认证成功后设置管道中的userId进行连接
        NettyUtil.setAttr(channel, NettyUtil.UID, user.getId());
        sendMsgByChannel(channel, WSResponse.build(SystemConstant.WS_HEAD_SHAKE_SUCCESS_MSG, "认证成功"));
        connect(channel);
    }

    @Override
    public void connect(Channel channel) {
        Long roomId = NettyUtil.getAttr(channel, NettyUtil.ROOM_ID);
        if (!ROOM_CHANNEL_MAP.containsKey(roomId)) {
            ROOM_CHANNEL_MAP.put(roomId, new CopyOnWriteArrayList<>());
        }
        ROOM_CHANNEL_MAP.get(roomId).add(channel);
    }

    @Override
    public void remove(Channel channel) {
        Long roomId = NettyUtil.getAttr(channel, NettyUtil.ROOM_ID);
        if (ROOM_CHANNEL_MAP.containsKey(roomId)) {
            ROOM_CHANNEL_MAP.get(roomId).remove(channel);
        }
    }

    @Override
    public void sendToAssignedRoom(Long roomId, WSResponse<?> wsResponse, Long skipUid) {
        if (ROOM_CHANNEL_MAP.containsKey(roomId)) {
            ROOM_CHANNEL_MAP.get(roomId).forEach(channel -> {
                Long uid = NettyUtil.getAttr(channel, NettyUtil.UID);
                if (uid != null && !uid.equals(skipUid)) {
                    SEND_MSG_TASK_EXECUTOR.execute(() -> sendMsgByChannel(channel, wsResponse));
                }
            });
        }
    }

    private void sendMsgByChannel(Channel channel, WSResponse<?> wsResponse) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(wsResponse)));
        if (wsResponse.getData() instanceof ShowMsgVO msg) {
            messageService.updateUserReadLatestMsg(NettyUtil.getAttr(channel, NettyUtil.UID), NettyUtil.getAttr(channel, NettyUtil.ROOM_ID),
                    msg.getMessage().getId());
        }

    }

    @Override
    public void showOnlineUser() {
        ROOM_CHANNEL_MAP.forEach((roomId, channels) -> {
            log.info("房间号: {}, 在线人数: {}", roomId, channels.size());
        });
    }
}
