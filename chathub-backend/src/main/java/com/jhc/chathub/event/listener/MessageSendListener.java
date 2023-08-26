package com.jhc.chathub.event.listener;

import com.jhc.chathub.common.constants.SystemConstant;
import com.jhc.chathub.common.resp.WSResponse;
import com.jhc.chathub.event.MessageSendEvent;
import com.jhc.chathub.pojo.entity.Message;
import com.jhc.chathub.pojo.vo.RoomVO;
import com.jhc.chathub.pojo.vo.ShowMsgVO;
import com.jhc.chathub.service.IMessageService;
import com.jhc.chathub.service.IRoomService;
import com.jhc.chathub.service.ISseService;
import com.jhc.chathub.service.IWebSocketService;
import com.jhc.chathub.sse.SseRespType;
import com.jhc.chathub.sse.SseResponse;
import com.jhc.chathub.sse.SseSessionManager;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class MessageSendListener {
    @Resource
    private IMessageService messageService;

    @Resource
    private IWebSocketService webSocketService;

    @Resource
    private ISseService sseService;

    @Resource
    private IRoomService roomService;

    // 线程池执行推送消息
    private static final ExecutorService SSE_SEND_MSG_TASK_EXECUTOR = Executors.newFixedThreadPool(10);


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

    @Async
    @TransactionalEventListener(classes = MessageSendEvent.class, fallbackExecution = true)
    public void notifyFreshMsgList(MessageSendEvent event) {
        // 1.查询哪些用户需要更新消息列表
        Message message = messageService.getById(event.getMsgId());
        List<Long> userIds = roomService.listUserIdsByRoomId(message.getRoomId());

        // 2.遍历用户列表，如果其存在SseEmitter，则发送消息
        userIds.forEach(userId -> {
            if (SseSessionManager.isExist(userId) && !webSocketService.isExist(userId)) {
                // 2.1构建信息列表的消息
                List<RoomVO> roomList = messageService.getRoomList(userId);
                // 2.2发送消息
                SSE_SEND_MSG_TASK_EXECUTOR.execute(() -> sseService.send(userId, SseResponse.build(SseRespType.FRESH_ROOM_LIST.getCode(), roomList)));
            }
        });
    }
}
