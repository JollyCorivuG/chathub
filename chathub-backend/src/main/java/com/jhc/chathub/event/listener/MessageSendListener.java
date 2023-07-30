package com.jhc.chathub.event.listener;

import com.jhc.chathub.common.constants.SystemConstant;
import com.jhc.chathub.common.resp.WSResponse;
import com.jhc.chathub.event.MessageSendEvent;
import com.jhc.chathub.pojo.entity.Message;
import com.jhc.chathub.pojo.vo.ShowMsgVO;
import com.jhc.chathub.service.IMessageService;
import com.jhc.chathub.service.IWebSocketService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class MessageSendListener {
    @Resource
    private IMessageService messageService;

    @Resource
    private IWebSocketService webSocketService;

    @Async
    @TransactionalEventListener(classes = MessageSendEvent.class, fallbackExecution = true)
    public void notifyOtherOnline(MessageSendEvent event) {
        Message message = messageService.getById(event.getMsgId());
        ShowMsgVO showMsg = messageService.convertToShowMsgVO(message);
        webSocketService.sendToAssignedRoom(
                message.getRoomId(),
                WSResponse.build(SystemConstant.WS_NORMAL_MSG, showMsg),
                message.getFromUserId()
        );
    }
}
