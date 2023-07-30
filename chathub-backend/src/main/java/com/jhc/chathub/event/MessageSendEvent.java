package com.jhc.chathub.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MessageSendEvent extends ApplicationEvent {
    private final Long msgId;

    public MessageSendEvent(Object source, Long msgId) {
        super(source);
        this.msgId = msgId;
    }
}
