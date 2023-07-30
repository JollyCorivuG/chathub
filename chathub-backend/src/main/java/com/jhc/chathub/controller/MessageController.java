package com.jhc.chathub.controller;

import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.pojo.dto.message.SendMsgDTO;
import com.jhc.chathub.pojo.entity.Message;
import com.jhc.chathub.pojo.vo.ShowMsgVO;
import com.jhc.chathub.service.IMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "消息相关接口")
public class MessageController {
    @Resource
    private IMessageService messageService;

    @PostMapping("/send")
    @Operation(summary = "发送消息")
    public Response<ShowMsgVO> sendMessage(@RequestBody SendMsgDTO sendMsg) {
        Message message = messageService.sendMsg(sendMsg);
        return Response.success(messageService.convertToShowMsgVO(message));
    }

}
