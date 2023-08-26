package com.jhc.chathub.controller;

import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.pojo.dto.group.CreateGroupDTO;
import com.jhc.chathub.pojo.dto.group.InvitationRespDTO;
import com.jhc.chathub.pojo.vo.GroupVO;
import com.jhc.chathub.service.IGroupService;
import com.jhc.chathub.utils.UserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: 群组处理类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/24
 */
@RestController
@RequestMapping("/groups")
@Tag(name = "群组相关接口")
public class GroupController {
    @Resource
    private IGroupService groupService;

    @PostMapping("/create")
    @Operation(summary = "创建群组")
    public Response<GroupVO> createGroup(@RequestBody CreateGroupDTO createGroup) {
        Long userId = UserHolder.getUser().getId();
        return Response.success(groupService.createGroup(userId, createGroup));
    }

    @GetMapping("/manage")
    @Operation(summary = "获取自己管理的群组")
    public Response<List<GroupVO>> getManageGroup() {
        Long userId = UserHolder.getUser().getId();
        return Response.success(groupService.getManageGroup(userId));
    }

    @GetMapping("/join")
    @Operation(summary = "获取自己加入的群组")
    public Response<List<GroupVO>> getJoinGroup() {
        Long userId = UserHolder.getUser().getId();
        return Response.success(groupService.getJoinGroup(userId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取特定群组信息")
    public Response<GroupVO> getGroupInfo(@PathVariable("id") Long groupId) {
        Long userId = UserHolder.getUser().getId();
        return Response.success(groupService.getGroupInfo(userId, groupId));
    }

    @PostMapping("/{id}/invite")
    @Operation(summary = "邀请用户加入群组")
    public Response<Void> inviteUsers(@RequestBody List<Long> inviteUserIds, @PathVariable("id") Long groupId) {
        Long userId = UserHolder.getUser().getId();
        groupService.inviteUsers(userId, inviteUserIds, groupId);
        return Response.success(null);
    }

    @PostMapping("/resp")
    @Operation(summary = "响应群组邀请")
    public Response<Void> invitationResp(@RequestBody InvitationRespDTO invitationResp) {
        Long userId = UserHolder.getUser().getId();
        groupService.invitationResp(userId, invitationResp);
        return Response.success(null);
    }
}
