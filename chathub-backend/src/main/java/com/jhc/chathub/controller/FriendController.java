package com.jhc.chathub.controller;

import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.pojo.dto.FriendApplication;
import com.jhc.chathub.pojo.dto.FriendApplicationReply;
import com.jhc.chathub.service.IFriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friends")
@Tag(name = "好友相关接口")
public class FriendController {
    @Resource
    private IFriendService friendService;

    @PostMapping("/application")
    @Operation(summary = "发送好友申请")
    public Response<Void> friendApplication(@RequestBody FriendApplication friendApplication) {
        return friendService.friendApplication(friendApplication);
    }

    @PostMapping("/application/reply")
    @Operation(summary = "接受或拒绝好友申请")
    public Response<Void> friendApplicationReply(@RequestBody FriendApplicationReply friendApplicationReply) {
        return friendService.acceptFriendApplication(friendApplicationReply);
    }

}
