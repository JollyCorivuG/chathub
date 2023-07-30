package com.jhc.chathub.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.chathub.common.constants.SystemConstant;
import com.jhc.chathub.event.MessageSendEvent;
import com.jhc.chathub.mapper.MessageMapper;
import com.jhc.chathub.pojo.dto.message.*;
import com.jhc.chathub.pojo.entity.Message;
import com.jhc.chathub.pojo.vo.ShowMsgVO;
import com.jhc.chathub.service.IMessageService;
import com.jhc.chathub.utils.UserHolder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

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
    public Message sendMsg(SendMsgDTO sendMsg) {
        // 1.将sendMsg转换为Message对象并保存到数据库
        Long userId = UserHolder.getUser().getId();
        Message saveMsg = new Message();
        saveMsg.setRoomId(sendMsg.getRoomId())
                .setFromUserId(userId)
                .setMsgType(sendMsg.getMsgType());
        setMsgExtra(saveMsg, sendMsg);
        save(saveMsg);

        // 2.发布消息发送事件
        applicationEventPublisher.publishEvent(new MessageSendEvent(this, saveMsg.getId()));

        // 3.返回保存的消息
        return saveMsg;
    }
}
