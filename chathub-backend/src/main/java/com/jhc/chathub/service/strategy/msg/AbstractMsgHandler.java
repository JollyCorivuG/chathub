package com.jhc.chathub.service.strategy.msg;

import com.jhc.chathub.pojo.dto.message.SendMsgDTO;
import com.jhc.chathub.pojo.entity.Message;
import com.jhc.chathub.pojo.enums.MsgTypeEnum;
import jakarta.annotation.PostConstruct;

/**
 * @Description: 消息处理器抽象类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/23
 */
public abstract class AbstractMsgHandler {
    @PostConstruct
    private void init() {
        MsgHandlerFactory.register(getMsgTypeEnum().getType(), this);
    }

    abstract MsgTypeEnum getMsgTypeEnum();

    public abstract void checkMsg(Long userId, SendMsgDTO sendMsg);

    public abstract Message saveMsg(Long userId, SendMsgDTO sendMsg);
}
