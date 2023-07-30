package com.jhc.chathub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.chathub.pojo.dto.message.SendMsgDTO;
import com.jhc.chathub.pojo.entity.Message;
import com.jhc.chathub.pojo.vo.ShowMsgVO;

public interface IMessageService extends IService<Message> {
    Message sendMsg(SendMsgDTO sendMsg);
    ShowMsgVO convertToShowMsgVO(Message message);
}
