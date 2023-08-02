package com.jhc.chathub.controller;

import com.jhc.chathub.common.resp.CursorPageBaseResp;
import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.pojo.dto.message.MsgPageReq;
import com.jhc.chathub.pojo.dto.message.SendMsgDTO;
import com.jhc.chathub.pojo.vo.ShowMsgVO;
import com.jhc.chathub.service.IMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "消息相关接口")
public class MessageController {
    @Resource
    private IMessageService messageService;

    @PostMapping("/send")
    @Operation(summary = "发送消息")
    public Response<ShowMsgVO> sendMessage(@RequestBody SendMsgDTO sendMsg) {
        Long msgId = messageService.sendMsg(sendMsg);
        return Response.success(messageService.convertToShowMsgVO(messageService.getById(msgId)));
    }

    @GetMapping("/list")
    @Operation(summary = "分页获取消息列表")
    public Response<CursorPageBaseResp<ShowMsgVO>> getMessageList(MsgPageReq msgPageReq) {
        CursorPageBaseResp<ShowMsgVO> msgPage = messageService.getMsgPage(msgPageReq);
        return Response.success(msgPage);
    }
}
