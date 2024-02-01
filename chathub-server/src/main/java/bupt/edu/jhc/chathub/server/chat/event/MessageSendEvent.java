package bupt.edu.jhc.chathub.server.chat.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @Description: 消息发送事件
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Getter
public class MessageSendEvent extends ApplicationEvent {
    private final Long msgId;

    public MessageSendEvent(Object source, Long msgId) {
        super(source);
        this.msgId = msgId;
    }
}
