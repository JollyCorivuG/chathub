package com.jhc.chathub.controller;

import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.mapper.FriendNoticeMapper;
import com.jhc.chathub.pojo.vo.FriendNoticeVO;
import com.jhc.chathub.service.IFriendNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notices")
@Tag(name = "通知相关接口")
public class NoticeController {
    @Resource
    private IFriendNoticeService friendNoticeService;
    @Resource
    private FriendNoticeMapper friendNoticeMapper;

    @GetMapping("/friends")
    @Operation(summary = "查询好友通知")
    public Response<List<FriendNoticeVO>> friendNoticeList() {
        return friendNoticeService.friendNoticeList();
    }

    @DeleteMapping("/friends/{id}")
    @Operation(summary = "删除好友通知")
    public Response<Void> deleteFriendNotice(@PathVariable Long id) {
        int isSuccess = friendNoticeMapper.deleteById(id);
        if (isSuccess == 0) {
            return Response.fail("删除该通知失败");
        } else {
            return Response.success(null);
        }
    }
}
