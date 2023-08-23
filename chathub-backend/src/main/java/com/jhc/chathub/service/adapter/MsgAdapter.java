package com.jhc.chathub.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.jhc.chathub.common.constants.SystemConstant;
import com.jhc.chathub.pojo.dto.message.SendMsgDTO;
import com.jhc.chathub.pojo.dto.message.TextMsgDTO;
import com.jhc.chathub.pojo.entity.Message;
import com.jhc.chathub.pojo.vo.ShowMsgVO;

import java.util.Objects;

/**
 * @Description: 消息适配器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/23
 */
public class MsgAdapter {
    public static Message buildMsgSave(Long userId, SendMsgDTO sendMsg) {
        Message message = new Message();
        message.setRoomId(sendMsg.getRoomId())
                .setFromUserId(userId)
                .setMsgType(sendMsg.getMsgType());
        return message;
    }

    public static ShowMsgVO.UserInfo buildFromUser(Long fromUserId) {
        return new ShowMsgVO.UserInfo().setId(fromUserId);
    }

    public static ShowMsgVO buildMsgResp(Message msg) {
        ShowMsgVO showMsg = new ShowMsgVO();
        showMsg.setFromUser(buildFromUser(msg.getFromUserId()));
        ShowMsgVO.MessageInfo msgInfo = new ShowMsgVO.MessageInfo();
        BeanUtil.copyProperties(msg, msgInfo);
        msgInfo.setSendTime(msg.getCreateTime());
        msgInfo.setBody(Objects.equals(msg.getMsgType(), SystemConstant.TEXT_MSG) ? new TextMsgDTO().setContent(msg.getContent()) : msg.getExtra());
        showMsg.setMessage(msgInfo);
        return showMsg;
    }
}
