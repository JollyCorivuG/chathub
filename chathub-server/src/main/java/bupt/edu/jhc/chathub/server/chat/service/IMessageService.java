package bupt.edu.jhc.chathub.server.chat.service;

import bupt.edu.jhc.chathub.common.domain.vo.resp.CursorPageBaseResp;
import bupt.edu.jhc.chathub.server.chat.domain.dto.MsgPageReq;
import bupt.edu.jhc.chathub.server.chat.domain.dto.SendMsgDTO;
import bupt.edu.jhc.chathub.server.chat.domain.entity.Message;
import bupt.edu.jhc.chathub.server.chat.domain.vo.RoomVO;
import bupt.edu.jhc.chathub.server.chat.domain.vo.ShowMsgVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 消息服务接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
public interface IMessageService extends IService<Message> {
    Long sendMsg(Long userId, SendMsgDTO sendMsg);

    void updateUserReadLatestMsg(Long userId, Long roomId, Long msgId);

    ShowMsgVO convertToShowMsgVO(Message message);

    CursorPageBaseResp<ShowMsgVO> getMsgPage(MsgPageReq msgPageReq);

    List<RoomVO> getRoomList(Long userId);

    void deleteRoom(Long userId, Long id);
}
