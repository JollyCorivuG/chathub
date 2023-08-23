package com.jhc.chathub.service.strategy.msg;

import cn.hutool.core.bean.BeanUtil;
import com.jhc.chathub.pojo.dto.message.FileMsgDTO;
import com.jhc.chathub.pojo.dto.message.MessageExtra;
import com.jhc.chathub.pojo.dto.message.SendMsgDTO;
import com.jhc.chathub.pojo.entity.Message;
import com.jhc.chathub.pojo.enums.MsgTypeEnum;
import com.jhc.chathub.service.adapter.MsgAdapter;
import org.springframework.stereotype.Component;

/**
 * @Description: 文件消息处理器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/23
 */
@Component
public class FileMsgHandler extends AbstractMsgHandler {
    /**
     * 获取消息类型
     * @return MsgTypeEnum
     */
    @Override
    MsgTypeEnum getMsgTypeEnum() {
        return MsgTypeEnum.FILE;
    }

    /**
     * 文件消息校验
     * @param userId
     * @param sendMsg
     */
    @Override
    public void checkMsg(Long userId, SendMsgDTO sendMsg) {
        // TODO 文件消息校验
    }

    /**
     * 保存消息
     * @param userId
     * @param sendMsg
     * @return Message
     */
    @Override
    public Message saveMsg(Long userId, SendMsgDTO sendMsg) {
        Message message = MsgAdapter.buildMsgSave(userId, sendMsg);
        FileMsgDTO fileMsg = BeanUtil.toBean(sendMsg.getBody(), FileMsgDTO.class);
        message.setExtra(new MessageExtra().setFileMsg(fileMsg));
        return message;
    }
}
