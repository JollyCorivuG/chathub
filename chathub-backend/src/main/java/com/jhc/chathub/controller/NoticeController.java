package com.jhc.chathub.controller;

import com.jhc.chathub.common.enums.ErrorStatus;
import com.jhc.chathub.common.exception.ThrowUtils;
import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.mapper.FriendNoticeMapper;
import com.jhc.chathub.pojo.vo.FriendNoticeVO;
import com.jhc.chathub.pojo.vo.GroupNoticeVO;
import com.jhc.chathub.service.IFriendNoticeService;
import com.jhc.chathub.service.IGroupNoticeService;
import com.jhc.chathub.utils.UserHolder;
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

    @Resource
    private IGroupNoticeService groupNoticeService;

    @GetMapping("/friends")
    @Operation(summary = "查询好友通知")
    public Response<List<FriendNoticeVO>> friendNoticeList() {
        return friendNoticeService.friendNoticeList();
    }

    @DeleteMapping("/friends/{id}")
    @Operation(summary = "删除好友通知")
    public Response<Void> deleteFriendNotice(@PathVariable Long id) {
        int isSuccess = friendNoticeMapper.deleteById(id);
        ThrowUtils.throwIf(isSuccess != 1, ErrorStatus.OPERATION_ERROR);
        return Response.success(null);
    }

    @GetMapping("/groups")
    @Operation(summary = "查询群组通知")
    public Response<List<GroupNoticeVO>> groupNoticeList() {
        Long userId = UserHolder.getUser().getId();
        return Response.success(groupNoticeService.getGroupNotice(userId));
    }

    @DeleteMapping("/groups/{id}")
    @Operation(summary = "删除群组通知")
    public Response<Void> deleteGroupNotice(@PathVariable Long id) {
        ThrowUtils.throwIf(!groupNoticeService.removeById(id), ErrorStatus.OPERATION_ERROR);
        return Response.success(null);
    }
}
