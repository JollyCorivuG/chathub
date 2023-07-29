package com.jhc.chathub.controller;

import com.jhc.chathub.common.constants.RedisConstant;
import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.mapper.UserMapper;
import com.jhc.chathub.pojo.dto.friend.FriendApplication;
import com.jhc.chathub.pojo.dto.friend.FriendApplicationReply;
import com.jhc.chathub.pojo.vo.UserVO;
import com.jhc.chathub.service.IFriendService;
import com.jhc.chathub.service.IUserService;
import com.jhc.chathub.utils.UserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/friends")
@Tag(name = "好友相关接口")
public class FriendController {
    @Resource
    private IFriendService friendService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IUserService userService;

    @Resource
    private UserMapper userMapper;

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

    @GetMapping("/list")
    @Operation(summary = "获取好友列表")
    public Response<List<UserVO>> getFriendList() {
        Long selfId = UserHolder.getUser().getId();
        Set<String> friendsId = stringRedisTemplate.opsForSet().members(RedisConstant.USER_FRIEND_KEY + selfId.toString());
        if (friendsId == null || friendsId.isEmpty()) {
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
                .toList();
        return Response.success(userVOList);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除好友")
    public Response<Void> deleteFriend(@PathVariable Long id) {
        return friendService.deleteFriend(id);
    }
}
