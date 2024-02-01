package bupt.edu.jhc.chathub.server.friend.controller;

import bupt.edu.jhc.chathub.common.domain.enums.ErrorStatus;
import bupt.edu.jhc.chathub.common.domain.vo.resp.Response;
import bupt.edu.jhc.chathub.common.utils.context.RequestHolder;
import bupt.edu.jhc.chathub.common.utils.exception.ThrowUtils;
import bupt.edu.jhc.chathub.server.friend.domain.vo.FriendNoticeVO;
import bupt.edu.jhc.chathub.server.friend.mapper.FriendNoticeMapper;
import bupt.edu.jhc.chathub.server.friend.service.IFriendNoticeService;
import bupt.edu.jhc.chathub.server.group.domain.vo.GroupNoticeVO;
import bupt.edu.jhc.chathub.server.group.service.IGroupNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 通知接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@RestController
@RequestMapping("/api/notices")
@Api(tags = "通知相关接口")
public class NoticeController {
    @Resource
    private IFriendNoticeService friendNoticeService;
    @Resource
    private FriendNoticeMapper friendNoticeMapper;

    @Resource
    private IGroupNoticeService groupNoticeService;

    @GetMapping("/friends")
    @ApiOperation("查询好友通知")
    public Response<List<FriendNoticeVO>> friendNoticeList() {
        return friendNoticeService.friendNoticeList();
    }

    @DeleteMapping("/friends/{id}")
    @ApiOperation("删除好友通知")
    public Response<Void> deleteFriendNotice(@PathVariable Long id) {
        int isSuccess = friendNoticeMapper.deleteById(id);
        ThrowUtils.throwIf(isSuccess != 1, ErrorStatus.OPERATION_ERROR);
        return Response.success(null);
    }

    @GetMapping("/groups")
    @ApiOperation("查询群组通知")
    public Response<List<GroupNoticeVO>> groupNoticeList() {
        Long userId = RequestHolder.get().getUid();
        return Response.success(groupNoticeService.getGroupNotice(userId));
    }

    @DeleteMapping("/groups/{id}")
    @ApiOperation("删除群组通知")
    public Response<Void> deleteGroupNotice(@PathVariable Long id) {
        ThrowUtils.throwIf(!groupNoticeService.removeById(id), ErrorStatus.OPERATION_ERROR);
        return Response.success(null);
    }
}

