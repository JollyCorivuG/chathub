package bupt.edu.jhc.chathub.server.group.controller;

import bupt.edu.jhc.chathub.common.domain.vo.resp.Response;
import bupt.edu.jhc.chathub.common.utils.context.RequestHolder;
import bupt.edu.jhc.chathub.server.group.domain.dto.CreateGroupDTO;
import bupt.edu.jhc.chathub.server.group.domain.dto.InvitationRespDTO;
import bupt.edu.jhc.chathub.server.group.domain.vo.GroupVO;
import bupt.edu.jhc.chathub.server.group.service.IGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 群组接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@RestController
@RequestMapping("/api/groups")
@Api(tags = "群组相关接口")
public class GroupController {
    @Resource
    private IGroupService groupService;

    @PostMapping("/create")
    @ApiOperation("创建群组")
    public Response<GroupVO> createGroup(@RequestBody CreateGroupDTO createGroup) {
        Long userId = RequestHolder.get().getUid();
        return Response.success(groupService.createGroup(userId, createGroup));
    }

    @GetMapping("/manage")
    @ApiOperation("获取自己管理的群组")
    public Response<List<GroupVO>> getManageGroup() {
        Long userId = RequestHolder.get().getUid();
        return Response.success(groupService.getManageGroup(userId));
    }

    @GetMapping("/join")
    @ApiOperation("获取自己加入的群组")
    public Response<List<GroupVO>> getJoinGroup() {
        Long userId = RequestHolder.get().getUid();
        return Response.success(groupService.getJoinGroup(userId));
    }

    @GetMapping("/{id}")
    @ApiOperation("获取特定群组信息")
    public Response<GroupVO> getGroupInfo(@PathVariable("id") Long groupId) {
        Long userId = RequestHolder.get().getUid();
        return Response.success(groupService.getGroupInfo(userId, groupId));
    }

    @PostMapping("/{id}/invite")
    @ApiOperation("邀请用户加入群组")
    public Response<Void> inviteUsers(@RequestBody List<Long> inviteUserIds, @PathVariable("id") Long groupId) {
        Long userId = RequestHolder.get().getUid();
        groupService.inviteUsers(userId, inviteUserIds, groupId);
        return Response.success(null);
    }

    @PostMapping("/resp")
    @ApiOperation("响应群组邀请")
    public Response<Void> invitationResp(@RequestBody InvitationRespDTO invitationResp) {
        Long userId = RequestHolder.get().getUid();
        groupService.invitationResp(userId, invitationResp);
        return Response.success(null);
    }
}
