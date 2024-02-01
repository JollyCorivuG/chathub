package bupt.edu.jhc.chathub.server.friend.controller;

import bupt.edu.jhc.chathub.common.domain.vo.resp.Response;
import bupt.edu.jhc.chathub.common.utils.context.RequestHolder;
import bupt.edu.jhc.chathub.server.friend.domain.dto.FriendApplication;
import bupt.edu.jhc.chathub.server.friend.domain.dto.FriendApplicationReply;
import bupt.edu.jhc.chathub.server.friend.service.IFriendService;
import bupt.edu.jhc.chathub.server.user.domain.vo.UserVO;
import bupt.edu.jhc.chathub.server.user.mapper.UserMapper;
import bupt.edu.jhc.chathub.server.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 好友接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@RestController
@RequestMapping("/api/friends")
@Api(tags = "好友相关接口")
public class FriendController {
    @Resource
    private IFriendService friendService;

    @Resource
    private IUserService userService;

    @Resource
    private UserMapper userMapper;

    @PostMapping("/application")
    @ApiOperation("发送好友申请")
    public Response<?> friendApplication(@RequestBody FriendApplication friendApplication) {
        return friendService.friendApplication(friendApplication);
    }

    @PostMapping("/application/reply")
    @ApiOperation("接受或拒绝好友申请")
    public Response<?> friendApplicationReply(@RequestBody FriendApplicationReply friendApplicationReply) {
        return friendService.acceptFriendApplication(friendApplicationReply);
    }

    @GetMapping("/list")
    @ApiOperation("获取好友列表")
    public Response<List<UserVO>> getFriendList() {
        Long selfId = RequestHolder.get().getUid();
        List<Long> friendsId = userService.queryFriendIds(selfId);
        if (friendsId.isEmpty()) {
            return Response.success(Collections.emptyList());
        }
        List<UserVO> userVOList = userMapper.selectBatchIds(friendsId).stream()
                .map(user -> userService.convertUserToUserVO(selfId, user))
                .sorted((o1, o2) -> {
                    if (o1.getIsOnline() && !o2.getIsOnline()) {
                        return -1;
                    } else if (!o1.getIsOnline() && o2.getIsOnline()) {
                        return 1;
                    } else {
                        return 0;
                    }
                })
                .collect(Collectors.toList());
        return Response.success(userVOList);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除好友")
    public Response<?> deleteFriend(@PathVariable Long id) {
        return friendService.deleteFriend(id);
    }
}
