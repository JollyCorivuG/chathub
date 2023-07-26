package com.jhc.chathub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.chathub.common.constants.RedisConstant;
import com.jhc.chathub.common.constants.SystemConstant;
import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.mapper.FriendNoticeMapper;
import com.jhc.chathub.mapper.FriendRelationMapper;
import com.jhc.chathub.mapper.UserMapper;
import com.jhc.chathub.pojo.dto.FriendApplication;
import com.jhc.chathub.pojo.dto.FriendApplicationReply;
import com.jhc.chathub.pojo.entity.FriendNotice;
import com.jhc.chathub.pojo.entity.FriendRelation;
import com.jhc.chathub.pojo.entity.User;
import com.jhc.chathub.service.IFriendService;
import com.jhc.chathub.utils.UserHolder;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class FriendServiceImpl extends ServiceImpl<FriendRelationMapper, FriendRelation> implements IFriendService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserMapper userMapper;

    @Resource
    private FriendNoticeMapper friendNoticeMapper;

    @Override
    @Transactional
    public Response<Void> friendApplication(FriendApplication friendApplication) {
        // 1.查询对方信息
        User user = userMapper.selectById(friendApplication.getToUserId());
        if (user == null) {
            return Response.fail("所要添加的用户不存在");
        }

        // 2.判断是否已经是好友
        Long selfId = UserHolder.getUser().getId();
        String key = RedisConstant.USER_FRIEND_KEY + selfId;
        if (Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(key, friendApplication.getToUserId().toString()))) {
            return Response.fail("该用户已经是你的好友");
        }

        // 3.创建两个通知
        // 3.1 创建自己的通知
        FriendNotice friendNotice1 = new FriendNotice();
        friendNotice1.setNoticeType(SystemConstant.NOTICE_TYPE_ADD_OTHER)
                .setConnectUserId(selfId)
                .setOtherUserId(friendApplication.getToUserId())
                .setDescription(friendApplication.getDescription())
                .setStatusInfo(SystemConstant.NOTICE_STATUS_WAIT);
        // 3.2 创建对方的通知
        FriendNotice friendNotice2 = new FriendNotice();
        friendNotice2.setNoticeType(SystemConstant.NOTICE_TYPE_OTHER_ADD_ME)
                .setConnectUserId(friendApplication.getToUserId())
                .setOtherUserId(selfId)
                .setDescription(friendApplication.getDescription())
                .setStatusInfo(SystemConstant.NOTICE_STATUS_PENDING);

        // 4.将通知存入数据库
        friendNoticeMapper.insert(friendNotice1);
        friendNoticeMapper.insert(friendNotice2);

        // 5.返回响应
        return Response.success(null);
    }

    @Override
    @Transactional
    public Response<Void> acceptFriendApplication(FriendApplicationReply friendApplicationReply) {
        // 1.查询通知
        Long selfId = UserHolder.getUser().getId();
        FriendNotice friendNotice = friendNoticeMapper.selectById(friendApplicationReply.getNoticeId());
        if (friendNotice == null || !Objects.equals(friendNotice.getConnectUserId(), selfId)) {
            return Response.fail("该通知不存在或不是自己的通知");
        }

        // 2.判断是否已经是好友
        String key = RedisConstant.USER_FRIEND_KEY + selfId;
        Long otherId = friendApplicationReply.getUserId();
        if (Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(key, otherId.toString()))) {
            return Response.fail("该用户已经是你的好友");
        }

        // 3.判断是同意还是拒绝
        boolean isAccept = friendApplicationReply.getIsAccept();
        if (!isAccept) {
            // 3.1修改通知状态
            // 3.1.1修改自己的通知
            friendNotice.setStatusInfo(SystemConstant.NOTICE_STATUS_REFUSE);
            friendNotice.setUpdateTime(null);
            friendNoticeMapper.updateById(friendNotice);
            // 3.1.2对方的通知也要修改, 根据selfId和otherId更新
            QueryWrapper<FriendNotice> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("connect_user_id", otherId)
                    .eq("other_user_id", selfId);
            FriendNotice otherNotice = new FriendNotice();
            otherNotice.setStatusInfo(SystemConstant.NOTICE_STATUS_NOT_PASS);
            friendNoticeMapper.update(otherNotice, queryWrapper);
        } else {
            // 3.1创建好友关系
            FriendRelation friendRelation = new FriendRelation();
            friendRelation.setUserId1(selfId)
                    .setUserId2(otherId);

            // 3.2将好友关系存入数据库
            save(friendRelation);

            // 3.3将好友关系存入redis
            stringRedisTemplate.opsForSet().add(key, otherId.toString());
            String otherKey = RedisConstant.USER_FRIEND_KEY + otherId;
            stringRedisTemplate.opsForSet().add(otherKey, selfId.toString());

            // 3.4修改通知状态
            // 3.4.1修改自己的通知
            friendNotice.setStatusInfo(SystemConstant.NOTICE_STATUS_ACCEPT);
            friendNotice.setUpdateTime(null);
            friendNoticeMapper.updateById(friendNotice);
            // 3.4.2对方的通知也要修改, 根据selfId和otherId更新
            QueryWrapper<FriendNotice> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("connect_user_id", otherId)
                    .eq("other_user_id", selfId);
            FriendNotice otherNotice = new FriendNotice();
            otherNotice.setStatusInfo(SystemConstant.NOTICE_STATUS_PASS);
            friendNoticeMapper.update(otherNotice, queryWrapper);
        }
        return Response.success(null);
    }
}
