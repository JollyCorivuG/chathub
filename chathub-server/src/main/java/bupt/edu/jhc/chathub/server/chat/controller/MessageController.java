package bupt.edu.jhc.chathub.server.chat.controller;

import bupt.edu.jhc.chathub.common.domain.vo.resp.CursorPageBaseResp;
import bupt.edu.jhc.chathub.common.domain.vo.resp.Response;
import bupt.edu.jhc.chathub.common.utils.context.RequestHolder;
import bupt.edu.jhc.chathub.server.chat.domain.dto.MsgPageReq;
import bupt.edu.jhc.chathub.server.chat.domain.dto.SendMsgDTO;
import bupt.edu.jhc.chathub.server.chat.domain.vo.RoomVO;
import bupt.edu.jhc.chathub.server.chat.domain.vo.ShowMsgVO;
import bupt.edu.jhc.chathub.server.chat.service.IMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 消息接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@RestController
@RequestMapping("/api/messages")
@Api(tags = "消息相关接口")
public class MessageController {
    @Resource
    private IMessageService messageService;

    @PostMapping("/send")
    @ApiOperation("发送消息")
    public Response<ShowMsgVO> sendMessage(@RequestBody SendMsgDTO sendMsg) {
        Long msgId = messageService.sendMsg(RequestHolder.get().getUid(), sendMsg);
        return Response.success(messageService.convertToShowMsgVO(messageService.getById(msgId)));
    }

    @GetMapping("/list")
    @ApiOperation("分页获取消息列表")
    public Response<CursorPageBaseResp<ShowMsgVO>> getMessageList(MsgPageReq msgPageReq) {
        CursorPageBaseResp<ShowMsgVO> msgPage = messageService.getMsgPage(msgPageReq);
        return Response.success(msgPage);
    }

    @GetMapping("/room")
    @ApiOperation("获取房间列表")
    public Response<List<RoomVO>> getRoomList() {
        Long userId = RequestHolder.get().getUid();
        List<RoomVO> roomList = messageService.getRoomList(userId);
        return Response.success(roomList);
    }

    @DeleteMapping("/room/{id}")
    @ApiOperation("删除房间")
    public Response<Void> deleteRoom(@PathVariable Long id) {
        Long userId = RequestHolder.get().getUid();
        messageService.deleteRoom(userId, id);
        return Response.success(null);
    }
}
