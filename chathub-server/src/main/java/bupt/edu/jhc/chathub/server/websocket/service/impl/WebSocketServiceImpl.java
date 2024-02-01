package bupt.edu.jhc.chathub.server.websocket.service.impl;

import bupt.edu.jhc.chathub.common.config.ThreadPoolConfig;
import bupt.edu.jhc.chathub.common.domain.constants.SystemConstants;
import bupt.edu.jhc.chathub.server.chat.domain.vo.ShowMsgVO;
import bupt.edu.jhc.chathub.server.chat.service.IMessageService;
import bupt.edu.jhc.chathub.server.user.domain.dto.UserDTO;
import bupt.edu.jhc.chathub.server.user.service.IUserService;
import bupt.edu.jhc.chathub.server.websocket.domain.resp.WSResponse;
import bupt.edu.jhc.chathub.server.websocket.service.IWebSocketService;
import bupt.edu.jhc.chathub.server.websocket.utils.NettyUtils;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Description: ws 服务实现类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
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
    @Resource(name = ThreadPoolConfig.WS_EXECUTOR)
    private ThreadPoolTaskExecutor SEND_MSG_TASK_EXECUTOR;

    // 记录所有连接到用户
    private static final ConcurrentHashSet<Long> ONLINE_USER_SET = new ConcurrentHashSet<>();

    @Override
    public void authorize(Channel channel) {
        // 1.得到channel中的token并进行认证
        String token = NettyUtils.getAttr(channel, NettyUtils.TOKEN);
        if (StrUtil.isBlank(token)) {
            sendMsgByChannel(channel, WSResponse.build(SystemConstants.WS_HEAD_SHAKE_FAIL_MSG, "认证失败"));
            channel.close();
            return;
        }
        UserDTO user = userService.getUserByToken(token);
        if (user == null) {
            sendMsgByChannel(channel, WSResponse.build(SystemConstants.WS_HEAD_SHAKE_FAIL_MSG, "认证失败"));
            channel.close();
            return;
        }

        // 2.认证成功后设置管道中的userId进行连接
        NettyUtils.setAttr(channel, NettyUtils.UID, user.getId());
        sendMsgByChannel(channel, WSResponse.build(SystemConstants.WS_HEAD_SHAKE_SUCCESS_MSG, "认证成功"));
        connect(channel);
    }

    @Override
    public void connect(Channel channel) {
        Long roomId = NettyUtils.getAttr(channel, NettyUtils.ROOM_ID);
        if (!ROOM_CHANNEL_MAP.containsKey(roomId)) {
            ROOM_CHANNEL_MAP.put(roomId, new CopyOnWriteArrayList<>());
        }
        ROOM_CHANNEL_MAP.get(roomId).add(channel);
        ONLINE_USER_SET.add(NettyUtils.getAttr(channel, NettyUtils.UID));
    }

    @Override
    public void remove(Channel channel) {
        Long roomId = NettyUtils.getAttr(channel, NettyUtils.ROOM_ID);
        if (ROOM_CHANNEL_MAP.containsKey(roomId)) {
            ROOM_CHANNEL_MAP.get(roomId).remove(channel);
            ONLINE_USER_SET.remove(NettyUtils.getAttr(channel, NettyUtils.UID));
        }
    }

    @Override
    public void sendToAssignedRoom(Long roomId, WSResponse<?> wsResponse, Long skipUid) {
        if (ROOM_CHANNEL_MAP.containsKey(roomId)) {
            ROOM_CHANNEL_MAP.get(roomId).forEach(channel -> {
                Long uid = NettyUtils.getAttr(channel, NettyUtils.UID);
                if (uid != null && !uid.equals(skipUid)) {
                    SEND_MSG_TASK_EXECUTOR.execute(() -> sendMsgByChannel(channel, wsResponse));
                }
            });
        }
    }

    private void sendMsgByChannel(Channel channel, WSResponse<?> wsResponse) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(wsResponse)));
        if (wsResponse.getData() instanceof ShowMsgVO) {
            ShowMsgVO msg = (ShowMsgVO) wsResponse.getData();
            messageService.updateUserReadLatestMsg(NettyUtils.getAttr(channel, NettyUtils.UID), NettyUtils.getAttr(channel, NettyUtils.ROOM_ID),
                    msg.getMessage().getId());
        }

    }

    @Override
    public void showOnlineUser() {
        log.info("当前在线人数: {}", ONLINE_USER_SET.size());
        ROOM_CHANNEL_MAP.forEach((roomId, channels) -> log.info("房间号: {}, 在线人数: {}", roomId, channels.size()));
    }

    @Override
    public boolean isExist(Long userId) {
        return ONLINE_USER_SET.contains(userId);
    }
}
