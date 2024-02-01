package bupt.edu.jhc.chathub.server.chat.service.strategy;


import bupt.edu.jhc.chathub.server.chat.domain.dto.SendMsgDTO;
import bupt.edu.jhc.chathub.server.chat.domain.entity.Message;
import bupt.edu.jhc.chathub.server.chat.domain.enums.MsgTypeEnum;

import javax.annotation.PostConstruct;

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
