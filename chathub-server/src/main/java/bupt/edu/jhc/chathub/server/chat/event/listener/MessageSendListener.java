package bupt.edu.jhc.chathub.server.chat.event.listener;

import bupt.edu.jhc.chathub.common.domain.constants.SystemConstants;
import bupt.edu.jhc.chathub.server.chat.domain.entity.Message;
import bupt.edu.jhc.chathub.server.chat.domain.vo.RoomVO;
import bupt.edu.jhc.chathub.server.chat.domain.vo.ShowMsgVO;
import bupt.edu.jhc.chathub.server.chat.event.MessageSendEvent;
import bupt.edu.jhc.chathub.server.chat.service.IMessageService;
import bupt.edu.jhc.chathub.server.chat.service.IRoomService;
import bupt.edu.jhc.chathub.server.sse.domain.enums.SseRespType;
import bupt.edu.jhc.chathub.server.sse.domain.vo.SseResponse;
import bupt.edu.jhc.chathub.server.sse.manager.SseSessionManager;
import bupt.edu.jhc.chathub.server.sse.service.ISseService;
import bupt.edu.jhc.chathub.server.websocket.domain.resp.WSResponse;
import bupt.edu.jhc.chathub.server.websocket.service.IWebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: 消息发送监听器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
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
                WSResponse.build(SystemConstants.WS_NORMAL_MSG, showMsg),
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
