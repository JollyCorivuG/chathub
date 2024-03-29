package bupt.edu.jhc.chathub.server.friend.service.impl;

import bupt.edu.jhc.chathub.common.domain.constants.RedisConstants;
import bupt.edu.jhc.chathub.common.domain.constants.SystemConstants;
import bupt.edu.jhc.chathub.common.domain.enums.ErrorStatus;
import bupt.edu.jhc.chathub.common.domain.vo.resp.Response;
import bupt.edu.jhc.chathub.common.utils.context.RequestHolder;
import bupt.edu.jhc.chathub.common.utils.exception.ThrowUtils;
import bupt.edu.jhc.chathub.server.chat.domain.entity.Room;
import bupt.edu.jhc.chathub.server.chat.domain.entity.UserRoom;
import bupt.edu.jhc.chathub.server.chat.mapper.RoomMapper;
import bupt.edu.jhc.chathub.server.chat.mapper.UserRoomMapper;
import bupt.edu.jhc.chathub.server.friend.domain.dto.FriendApplication;
import bupt.edu.jhc.chathub.server.friend.domain.dto.FriendApplicationReply;
import bupt.edu.jhc.chathub.server.friend.domain.entity.FriendNotice;
import bupt.edu.jhc.chathub.server.friend.domain.entity.FriendRelation;
import bupt.edu.jhc.chathub.server.friend.mapper.FriendNoticeMapper;
import bupt.edu.jhc.chathub.server.friend.mapper.FriendRelationMapper;
import bupt.edu.jhc.chathub.server.friend.service.IFriendService;
import bupt.edu.jhc.chathub.server.user.domain.entity.User;
import bupt.edu.jhc.chathub.server.user.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Description: 好友服务实现类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Service
public class FriendServiceImpl extends ServiceImpl<FriendRelationMapper, FriendRelation> implements IFriendService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IUserService userService;

    @Resource
    private FriendNoticeMapper friendNoticeMapper;

    @Resource
    private RoomMapper roomMapper;

    @Resource
    private UserRoomMapper userRoomMapper;

    private boolean isFriend(Long selfId, Long otherId) {
        String key = RedisConstants.USER_FRIEND_KEY + selfId;
        return Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(key, otherId.toString()));
    }

    @Override
    @Transactional
    public Response<Void> friendApplication(FriendApplication friendApplication) {
        // 1.查询对方信息
        User user = userService.getById(friendApplication.getToUserId());
        ThrowUtils.throwIf(user == null, ErrorStatus.PARAMS_ERROR);

        // 2.判断是否已经是好友
        Long selfId = RequestHolder.get().getUid();
        ThrowUtils.throwIf(isFriend(selfId, friendApplication.getToUserId()), ErrorStatus.PARAMS_ERROR, "该用户已经是你的好友");

        // 3.创建两个通知
        // 3.1 创建自己的通知
        FriendNotice friendNotice1 = new FriendNotice();
        friendNotice1.setNoticeType(SystemConstants.NOTICE_TYPE_ADD_OTHER)
                .setConnectUserId(selfId)
                .setOtherUserId(friendApplication.getToUserId())
                .setDescription(friendApplication.getDescription())
                .setStatusInfo(SystemConstants.NOTICE_STATUS_WAIT);
        // 3.2 创建对方的通知
        FriendNotice friendNotice2 = new FriendNotice();
        friendNotice2.setNoticeType(SystemConstants.NOTICE_TYPE_OTHER_ADD_ME)
                .setConnectUserId(friendApplication.getToUserId())
                .setOtherUserId(selfId)
                .setDescription(friendApplication.getDescription())
                .setStatusInfo(SystemConstants.NOTICE_STATUS_PENDING);

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
        Long selfId = RequestHolder.get().getUid();
        FriendNotice friendNotice = friendNoticeMapper.selectById(friendApplicationReply.getNoticeId());
        ThrowUtils.throwIf(friendNotice == null || !Objects.equals(friendNotice.getConnectUserId(), selfId),
                ErrorStatus.NOT_FOUND_ERROR, "该通知不存在或不是自己的通知");

        // 2.判断是否已经是好友
        String key = RedisConstants.USER_FRIEND_KEY + selfId;
        Long otherId = friendApplicationReply.getUserId();
        ThrowUtils.throwIf(Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(key, otherId.toString())),
                ErrorStatus.PARAMS_ERROR, "该用户已经是你的好友");

        // 3.判断是同意还是拒绝
        boolean isAccept = friendApplicationReply.getIsAccept();
        if (!isAccept) {
            // 3.1修改通知状态
            // 3.1.1修改自己的通知
            friendNotice.setStatusInfo(SystemConstants.NOTICE_STATUS_REFUSE);
            friendNotice.setUpdateTime(null);
            friendNoticeMapper.updateById(friendNotice);
            // 3.1.2对方的通知也要修改, 根据selfId和otherId更新
            QueryWrapper<FriendNotice> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("connect_user_id", otherId)
                    .eq("other_user_id", selfId);
            FriendNotice otherNotice = new FriendNotice();
            otherNotice.setStatusInfo(SystemConstants.NOTICE_STATUS_NOT_PASS);
            friendNoticeMapper.update(otherNotice, queryWrapper);
        } else {
            // 3.1将好友关系存入redis
            stringRedisTemplate.opsForSet().add(key, otherId.toString());
            String otherKey = RedisConstants.USER_FRIEND_KEY + otherId;
            stringRedisTemplate.opsForSet().add(otherKey, selfId.toString());

            // 3.2创建好友关系以及会话id
            FriendRelation friendRelation = new FriendRelation();
            Room room = new Room();
            room.setRoomType(SystemConstants.ROOM_TYPE_SINGLE);
            roomMapper.insert(room);
            friendRelation.setUserId1(selfId)
                    .setUserId2(otherId)
                    .setRoomId(room.getId());

            // 3.3将好友关系存入数据库, 更新双方好友数量
            save(friendRelation);
            userService.update().setSql("friend_count = friend_count + 1").eq("id", selfId).update();
            userService.update().setSql("friend_count = friend_count + 1").eq("id", otherId).update();

            // 3.4修改通知状态
            // 3.4.1修改自己的通知
            friendNotice.setStatusInfo(SystemConstants.NOTICE_STATUS_ACCEPT);
            friendNotice.setUpdateTime(null);
            friendNoticeMapper.updateById(friendNotice);
            // 3.4.2对方的通知也要修改, 根据selfId和otherId更新
            QueryWrapper<FriendNotice> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("connect_user_id", otherId)
                    .eq("other_user_id", selfId);
            FriendNotice otherNotice = new FriendNotice();
            otherNotice.setStatusInfo(SystemConstants.NOTICE_STATUS_PASS);
            friendNoticeMapper.update(otherNotice, queryWrapper);

            // 3.5创建 userRoom
            UserRoom userRoom1 = new UserRoom();
            userRoom1.setRoomId(room.getId())
                    .setUserId(selfId);
            UserRoom userRoom2 = new UserRoom();
            userRoom2.setRoomId(room.getId())
                    .setUserId(otherId);
            userRoomMapper.insert(userRoom1);
            userRoomMapper.insert(userRoom2);
        }
        return Response.success(null);
    }

    @Override
    @Transactional
    public Response<Void> deleteFriend(Long id) {
        // 1.先判断是否是好友
        Long selfId = RequestHolder.get().getUid();
        ThrowUtils.throwIf(!isFriend(selfId, id), ErrorStatus.OPERATION_ERROR, "该用户不是你的好友");

        // 2.再删除redis中的好友关系
        stringRedisTemplate.opsForSet().remove(RedisConstants.USER_FRIEND_KEY + selfId, id.toString());
        stringRedisTemplate.opsForSet().remove(RedisConstants.USER_FRIEND_KEY + id, selfId.toString());

        // 3.最后删除数据库中的好友关系, 以及扣减双方好友数量
        QueryWrapper<FriendRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("user_id1", selfId)
                .eq("user_id2", id)
                .or()
                .eq("user_id1", id)
                .eq("user_id2", selfId);
        FriendRelation friendRelation = getOne(queryWrapper);
        roomMapper.deleteById(friendRelation.getRoomId());
        remove(queryWrapper);
        userService.update().setSql("friend_count = friend_count - 1").eq("id", selfId).update();
        userService.update().setSql("friend_count = friend_count - 1").eq("id", id).update();

        // 4.返回响应
        return Response.success(null);
    }

    @Override
    public Long getRoomId(Long selfId, Long otherId) {
        if (!isFriend(selfId, otherId)) {
            throw new RuntimeException("该用户不是你的好友");
        }
        QueryWrapper<FriendRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("user_id1", selfId)
                .eq("user_id2", otherId)
                .or()
                .eq("user_id1", otherId)
                .eq("user_id2", selfId);
        FriendRelation friendRelation = getOne(queryWrapper);
        return friendRelation.getRoomId();
    }
}
