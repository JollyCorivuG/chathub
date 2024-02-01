package bupt.edu.jhc.chathub.server.chat.service.adapter;

import bupt.edu.jhc.chathub.common.domain.constants.SystemConstants;
import bupt.edu.jhc.chathub.server.chat.domain.dto.SendMsgDTO;
import bupt.edu.jhc.chathub.server.chat.domain.dto.TextMsgDTO;
import bupt.edu.jhc.chathub.server.chat.domain.entity.Message;
import bupt.edu.jhc.chathub.server.chat.domain.vo.ShowMsgVO;
import cn.hutool.core.bean.BeanUtil;

import java.util.Objects;

/**
 * @Description: 消息适配器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
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
        msgInfo.setBody(Objects.equals(msg.getMsgType(), SystemConstants.TEXT_MSG) ? new TextMsgDTO().setContent(msg.getContent()) : msg.getExtra());
        showMsg.setMessage(msgInfo);
        return showMsg;
    }
}