package com.jhc.chathub.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.chathub.common.constants.RedisConstant;
import com.jhc.chathub.common.constants.SystemConstant;
import com.jhc.chathub.common.resp.CursorPageBaseResp;
import com.jhc.chathub.event.MessageSendEvent;
import com.jhc.chathub.mapper.MessageMapper;
import com.jhc.chathub.pojo.dto.message.*;
import com.jhc.chathub.pojo.entity.Message;
import com.jhc.chathub.pojo.vo.RoomVO;
import com.jhc.chathub.pojo.vo.ShowMsgVO;
import com.jhc.chathub.pojo.vo.UserVO;
import com.jhc.chathub.service.IFriendService;
import com.jhc.chathub.service.IMessageService;
import com.jhc.chathub.service.IUserService;
import com.jhc.chathub.utils.CursorUtils;
import com.jhc.chathub.utils.UserHolder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IUserService userService;

    @Resource
    private IFriendService friendService;


    private void setMsgExtra(Message message, SendMsgDTO sendMsg) throws IllegalArgumentException {
        Integer msgType = sendMsg.getMsgType();
        if (Objects.equals(msgType, SystemConstant.TEXT_MSG)) {
            TextMsgDTO textMsg = BeanUtil.toBean(sendMsg.getBody(), TextMsgDTO.class);
            message.setContent(textMsg.getContent());
        } else if (Objects.equals(msgType, SystemConstant.IMG_MSG)) {
            ImgMsgDTO imgMsg = BeanUtil.toBean(sendMsg.getBody(), ImgMsgDTO.class);
            message.setExtra(new MessageExtra().setImgMsg(imgMsg));
        } else if (Objects.equals(msgType, SystemConstant.FILE_MSG)) {
            FileMsgDTO fileMsg = BeanUtil.toBean(sendMsg.getBody(), FileMsgDTO.class);
            message.setExtra(new MessageExtra().setFileMsg(fileMsg));
        } else {
            // 抛出异常
            log.info("未知消息类型");
            throw new IllegalArgumentException("未知消息类型");
        }
    }

    @Override
    public void updateUserReadLatestMsg(Long userId, Long roomId, Long msgId) {
        String userLatestMsgKey = RedisConstant.USER_READ_LATEST_MESSAGE + userId + ":" + roomId;
        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(userLatestMsgKey))) {
            stringRedisTemplate.opsForValue().set(userLatestMsgKey, String.valueOf(msgId));
        } else {
            Long userLatestMsgId = Long.valueOf(Objects.requireNonNull(stringRedisTemplate.opsForValue().get(userLatestMsgKey)));
            if (msgId > userLatestMsgId) {
                stringRedisTemplate.opsForValue().set(userLatestMsgKey, String.valueOf(msgId));
            }
        }
    }

    @Override
    public ShowMsgVO convertToShowMsgVO(Message message) {
        ShowMsgVO showMsg = new ShowMsgVO();
        showMsg.setFromUser(new ShowMsgVO.UserInfo().setId(message.getFromUserId()));
        ShowMsgVO.MessageInfo messageInfo = new ShowMsgVO.MessageInfo();
        BeanUtil.copyProperties(message, messageInfo);
        messageInfo.setSendTime(message.getCreateTime());
        if (Objects.equals(message.getMsgType(), SystemConstant.TEXT_MSG)) {
            TextMsgDTO textMsg = new TextMsgDTO().setContent(message.getContent());
            messageInfo.setBody(textMsg);
        } else {
            messageInfo.setBody(message.getExtra());
        }
        showMsg.setMessage(messageInfo);
        return showMsg;
    }

    @Override
    public Long sendMsg(SendMsgDTO sendMsg) {
        // 1.将sendMsg转换为Message对象并保存到数据库
        Long userId = UserHolder.getUser().getId();
        Message saveMsg = new Message();
        saveMsg.setRoomId(sendMsg.getRoomId())
                .setFromUserId(userId)
                .setMsgType(sendMsg.getMsgType());
        setMsgExtra(saveMsg, sendMsg);
        save(saveMsg);
        stringRedisTemplate.opsForValue().set(RedisConstant.ROOM_LATEST_MESSAGE + sendMsg.getRoomId(), String.valueOf(saveMsg.getId()));
        updateUserReadLatestMsg(userId, sendMsg.getRoomId(), saveMsg.getId());

        // 2.发布消息发送事件
        applicationEventPublisher.publishEvent(new MessageSendEvent(this, saveMsg.getId()));

        // 3.返回保存的消息
        return saveMsg.getId();
    }

    @Override
    public CursorPageBaseResp<ShowMsgVO> getMsgPage(MsgPageReq msgPageReq) {
        // 1.查询出CursorPageBaseResp<Message>
        CursorPageBaseResp<Message> msgPage = CursorUtils.getCursorPageByMysql(this, msgPageReq, wrapper -> wrapper.eq(Message::getRoomId, msgPageReq.getRoomId()), Message::getId);
        if (msgPage.isEmpty()) {
            return CursorPageBaseResp.empty();
        }
        if (msgPageReq.isFirstPage()) {
            updateUserReadLatestMsg(UserHolder.getUser().getId(), msgPageReq.getRoomId(), msgPage.getList().get(0).getId());
        }

        // 2.将msgPage中的list类型转换为List<ShowMsgVO>
        return CursorPageBaseResp.change(msgPage, msgPage.getList().stream().map(this::convertToShowMsgVO).toList());
    }

    private List<RoomVO> getFriendRooms(Long selfId, List<Long> friendIds) {
        List<RoomVO> rooms = new ArrayList<>();
        friendIds.forEach(friendId -> {
            Long roomId = friendService.getRoomId(selfId, friendId);
            String latestMsgId = stringRedisTemplate.opsForValue().get(RedisConstant.ROOM_LATEST_MESSAGE + roomId);
            if (latestMsgId == null) {
                return;
            }
            String latestDeleteMsgId = stringRedisTemplate.opsForValue().get(RedisConstant.USER_DELETE_LATEST_MESSAGE + selfId + ":" + roomId);
            if (latestDeleteMsgId != null && Long.parseLong(latestDeleteMsgId) >= Long.parseLong(latestMsgId)) {
                return;
            }
            ShowMsgVO message = convertToShowMsgVO(getById(Long.parseLong(latestMsgId)));
            String latestReadMsgId = stringRedisTemplate.opsForValue().get(RedisConstant.USER_READ_LATEST_MESSAGE + selfId + ":" + roomId);
            QueryWrapper<Message> wrapper = new QueryWrapper<>();
            if (latestReadMsgId != null) {
                wrapper.gt("id", latestReadMsgId);
            }
            wrapper.eq("room_id", roomId);
            long unreadCount = count(wrapper);

            UserVO friendInfo = userService.convertUserToUserVO(selfId, userService.getById(friendId));
            RoomVO room = new RoomVO();
            room.setId(roomId)
                    .setRoomType(SystemConstant.ROOM_TYPE_SINGLE)
                    .setConnectInfo(friendInfo)
                    .setLatestMsg(message)
                    .setUnreadCount((int) unreadCount);
            rooms.add(room);
        });
        return rooms;
    }

    private List<RoomVO> getGroupRooms(Long selfId, List<Long> groupIds) {
        return Collections.emptyList();
    }

    @Override
    public List<RoomVO> getRoomList() {
        // 1.先得到用户的好友id和群组id
        Long userId = UserHolder.getUser().getId();
        List<Long> friendIds = userService.queryFriendIds(userId);
        List<Long> groupIds = userService.queryGroupIds(userId);
        List<RoomVO> friendRooms = getFriendRooms(userId, friendIds);
        List<RoomVO> groupRooms = getGroupRooms(userId, groupIds);

        // 2.将friendRooms和groupRooms合并, 并按照最后一条消息的时间倒序排序
        List<RoomVO> rooms = new ArrayList<>(friendRooms.size() + groupRooms.size());
        rooms.addAll(friendRooms);
        rooms.addAll(groupRooms);
        rooms.sort((o1, o2) -> {
            if (o1.getLatestMsg() == null) {
                return 1;
            }
            if (o2.getLatestMsg() == null) {
                return -1;
            }
            return o2.getLatestMsg().getMessage().getSendTime().compareTo(o1.getLatestMsg().getMessage().getSendTime());
        });
        return rooms;
    }
}
