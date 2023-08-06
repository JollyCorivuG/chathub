package com.jhc.chathub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.chathub.common.resp.CursorPageBaseResp;
import com.jhc.chathub.pojo.dto.message.MsgPageReq;
import com.jhc.chathub.pojo.dto.message.SendMsgDTO;
import com.jhc.chathub.pojo.entity.Message;
import com.jhc.chathub.pojo.vo.RoomVO;
import com.jhc.chathub.pojo.vo.ShowMsgVO;

import java.util.List;

public interface IMessageService extends IService<Message> {
    Long sendMsg(SendMsgDTO sendMsg);

    void updateUserReadLatestMsg(Long userId, Long roomId, Long msgId);

    ShowMsgVO convertToShowMsgVO(Message message);

    CursorPageBaseResp<ShowMsgVO> getMsgPage(MsgPageReq msgPageReq);

    List<RoomVO> getRoomList();

    void deleteRoom(Long userId, Long id);
}
