package bupt.edu.jhc.chathub.server.chat.service.impl;

import bupt.edu.jhc.chathub.common.domain.constants.SystemConstants;
import bupt.edu.jhc.chathub.common.domain.enums.ErrorStatus;
import bupt.edu.jhc.chathub.common.domain.vo.resp.CursorPageBaseResp;
import bupt.edu.jhc.chathub.common.utils.CursorUtils;
import bupt.edu.jhc.chathub.common.utils.context.RequestHolder;
import bupt.edu.jhc.chathub.common.utils.exception.ThrowUtils;
import bupt.edu.jhc.chathub.server.chat.domain.dto.MsgPageReq;
import bupt.edu.jhc.chathub.server.chat.domain.dto.SendMsgDTO;
import bupt.edu.jhc.chathub.server.chat.domain.entity.Message;
import bupt.edu.jhc.chathub.server.chat.domain.entity.UserRoom;
import bupt.edu.jhc.chathub.server.chat.domain.vo.RoomVO;
import bupt.edu.jhc.chathub.server.chat.domain.vo.ShowMsgVO;
import bupt.edu.jhc.chathub.server.chat.event.MessageSendEvent;
import bupt.edu.jhc.chathub.server.chat.mapper.MessageMapper;
import bupt.edu.jhc.chathub.server.chat.mapper.UserRoomMapper;
import bupt.edu.jhc.chathub.server.chat.service.IMessageService;
import bupt.edu.jhc.chathub.server.chat.service.IRoomService;
import bupt.edu.jhc.chathub.server.chat.service.adapter.MsgAdapter;
import bupt.edu.jhc.chathub.server.chat.service.cache.RoomLatestMsgCache;
import bupt.edu.jhc.chathub.server.chat.service.cache.UserRoomCache;
import bupt.edu.jhc.chathub.server.chat.service.strategy.AbstractMsgHandler;
import bupt.edu.jhc.chathub.server.chat.service.strategy.MsgHandlerFactory;
import bupt.edu.jhc.chathub.server.friend.domain.entity.FriendRelation;
import bupt.edu.jhc.chathub.server.friend.service.IFriendService;
import bupt.edu.jhc.chathub.server.group.domain.entity.Group;
import bupt.edu.jhc.chathub.server.group.domain.vo.GroupVO;
import bupt.edu.jhc.chathub.server.group.service.IGroupService;
import bupt.edu.jhc.chathub.server.user.domain.vo.UserVO;
import bupt.edu.jhc.chathub.server.user.service.IUserService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import kotlin.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description: 消息服务实现类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Service
@Slf4j
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IUserService userService;

    @Resource
    private IFriendService friendService;

    @Resource
    private IGroupService groupService;

    @Resource
    private RoomLatestMsgCache roomLatestMsgCache;

    @Resource
    private UserRoomCache userRoomCache;

    @Resource
    private IRoomService roomService;

    @Resource
    private UserRoomMapper userRoomMapper;

    @Override
    public void updateUserReadLatestMsg(Long userId, Long roomId, Long msgId) {
//        String userLatestMsgKey = RedisConstants.USER_READ_LATEST_MESSAGE + userId + ":" + roomId;
//        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(userLatestMsgKey))) {
//            stringRedisTemplate.opsForValue().set(userLatestMsgKey, String.valueOf(msgId));
//        } else {
//            Long userLatestMsgId = Long.valueOf(Objects.requireNonNull(stringRedisTemplate.opsForValue().get(userLatestMsgKey)));
//            if (msgId > userLatestMsgId) {
//                stringRedisTemplate.opsForValue().set(userLatestMsgKey, String.valueOf(msgId));
//            }
//        }
        QueryWrapper<UserRoom> userRoomQueryWrapper = new QueryWrapper<>();
        userRoomQueryWrapper.eq("user_id", userId).eq("room_id", roomId);
        UserRoom userRoom = userRoomMapper.selectList(userRoomQueryWrapper).isEmpty() ? null : userRoomMapper.selectList(userRoomQueryWrapper).get(0);
        if (Objects.isNull(userRoom)) {
            userRoom = new UserRoom();
            userRoom.setUserId(userId).setRoomId(roomId).setLatestReadMsgId(msgId);
            userRoomMapper.insert(userRoom);
        } else {
            if (msgId > userRoom.getLatestReadMsgId()) {
                userRoom.setLatestReadMsgId(msgId);
                userRoomMapper.updateById(userRoom);
            }
        }
        userRoomCache.del(new Pair<>(userId, roomId));
    }

    @Override
    public ShowMsgVO convertToShowMsgVO(Message message) {
        return MsgAdapter.buildMsgResp(message);
    }

    @Override
    @Transactional
    public Long sendMsg(Long userId, SendMsgDTO sendMsg) {
        // 1.将sendMsg转换为Message对象并保存到数据库
        AbstractMsgHandler msgHandler = MsgHandlerFactory.getStrategy(sendMsg.getMsgType());
        msgHandler.checkMsg(userId, sendMsg);
        Message save = msgHandler.saveMsg(userId, sendMsg);
        save(save);

        // 2.更新redis中房间最新消息id以及用户最新读取消息id
//        stringRedisTemplate.opsForValue().set(RedisConstants.ROOM_LATEST_MESSAGE + sendMsg.getRoomId(), String.valueOf(save.getId()));
        // 更新房间最新消息 id
        ThrowUtils.throwIf(!roomService.update()
                .eq("id", sendMsg.getRoomId())
                .set("latest_msg_id", save.getId())
                .update(), ErrorStatus.SYSTEM_ERROR, "更新房间最新消息失败");
        // 删除缓存
        roomLatestMsgCache.del(sendMsg.getRoomId());
        updateUserReadLatestMsg(userId, sendMsg.getRoomId(), save.getId());

        // 3.如果这是第一次发送消息, 则需要判断是否需要创建房间(处理对方不在 ws 连接的情况)
        FriendRelation fl = friendService.lambdaQuery().eq(FriendRelation::getRoomId, sendMsg.getRoomId()).one();
        UserRoom userRoom = userRoomMapper.selectOne(new QueryWrapper<UserRoom>().eq("user_id", fl.getUserId1() - userId + fl.getUserId2()).eq("room_id", sendMsg.getRoomId()));
        if (Objects.isNull(userRoom)) {
            userRoom = new UserRoom();
            userRoom.setUserId(fl.getUserId1() - userId + fl.getUserId2()).setRoomId(sendMsg.getRoomId()).setLatestReadMsgId(save.getId());
            userRoomMapper.insert(userRoom);
        }

        // 4.发布消息发送事件
        applicationEventPublisher.publishEvent(new MessageSendEvent(this, save.getId()));

        // 5.返回保存的消息
        return save.getId();
    }

    @Override
    public CursorPageBaseResp<ShowMsgVO> getMsgPage(MsgPageReq msgPageReq) {
        // 1.查询出CursorPageBaseResp<Message>
        CursorPageBaseResp<Message> msgPage = CursorUtils.getCursorPageByMysql(this, msgPageReq, wrapper -> wrapper.eq(Message::getRoomId, msgPageReq.getRoomId()), Message::getId);
        if (msgPage.isEmpty()) {
            return CursorPageBaseResp.empty();
        }
        if (msgPageReq.isFirstPage()) {
            updateUserReadLatestMsg(RequestHolder.get().getUid(), msgPageReq.getRoomId(), msgPage.getList().get(0).getId());
        }

        // 2.将msgPage中的list类型转换为List<ShowMsgVO>
        return CursorPageBaseResp.change(msgPage, msgPage.getList().stream().map(this::convertToShowMsgVO).collect(Collectors.toList()));
    }

    private List<RoomVO> getFriendRooms(Long selfId, List<Long> friendIds) {
        List<RoomVO> rooms = new ArrayList<>();
        friendIds.forEach(friendId -> {
            Long roomId = friendService.getRoomId(selfId, friendId);
//            String latestMsgId = stringRedisTemplate.opsForValue().get(RedisConstants.ROOM_LATEST_MESSAGE + roomId);
            Long latestMsgId = roomLatestMsgCache.get(roomId);
            if (Objects.isNull(latestMsgId)) {
                return;
            }

            UserRoom userRoom = userRoomCache.get(new Pair<>(selfId, roomId));
//            String latestDeleteMsgId = stringRedisTemplate.opsForValue().get(RedisConstants.USER_DELETE_LATEST_MESSAGE + selfId + ":" + roomId);
            if (userRoom.getLatestDelMsgId() != null && userRoom.getLatestDelMsgId() >= latestMsgId) {
                return;
            }
            ShowMsgVO message = convertToShowMsgVO(getById(latestMsgId));
//            String latestReadMsgId = stringRedisTemplate.opsForValue().get(RedisConstants.USER_READ_LATEST_MESSAGE + selfId + ":" + roomId);
            Long latestReadMsgId = userRoom.getLatestReadMsgId();
            QueryWrapper<Message> wrapper = new QueryWrapper<>();
            if (!Objects.isNull(latestReadMsgId)) {
                wrapper.gt("id", latestReadMsgId);
            }
            wrapper.eq("room_id", roomId);
            long unreadCount = count(wrapper);
            UserVO friendInfo = userService.convertUserToUserVO(selfId, userService.getById(friendId));
            RoomVO room = new RoomVO();
            room.setId(roomId)
                    .setRoomType(SystemConstants.ROOM_TYPE_SINGLE)
                    .setConnectInfo(friendInfo)
                    .setLatestMsg(message)
                    .setUnreadCount((int) unreadCount);
            rooms.add(room);
        });
        return rooms;
    }

    private List<RoomVO> getGroupRooms(Long selfId, List<Long> groupIds) {
        List<RoomVO> rooms = new ArrayList<>();
        groupIds.forEach(groupId -> {
            Group group = groupService.getById(groupId);
            Long roomId = group.getRoomId();
//            String latestMsgId = stringRedisTemplate.opsForValue().get(RedisConstants.ROOM_LATEST_MESSAGE + roomId);
            Long latestMsgId = roomLatestMsgCache.get(roomId);
            if (Objects.isNull(latestMsgId)) {
                return;
            }

            UserRoom userRoom = userRoomCache.get(new Pair<>(selfId, roomId));
//            String latestDeleteMsgId = stringRedisTemplate.opsForValue().get(RedisConstants.USER_DELETE_LATEST_MESSAGE + selfId + ":" + roomId);
            if (userRoom.getLatestDelMsgId() != null && userRoom.getLatestDelMsgId() >= latestMsgId) {
                return;
            }
            ShowMsgVO message = convertToShowMsgVO(getById(latestMsgId));
//            String latestReadMsgId = stringRedisTemplate.opsForValue().get(RedisConstants.USER_READ_LATEST_MESSAGE + selfId + ":" + roomId);
            Long latestReadMsgId = userRoom.getLatestReadMsgId();
            QueryWrapper<Message> wrapper = new QueryWrapper<>();
            if (latestReadMsgId != null) {
                wrapper.gt("id", latestReadMsgId);
            }
            wrapper.eq("room_id", roomId);
            long unreadCount = count(wrapper);

            RoomVO room = new RoomVO();
            room.setId(roomId)
                    .setRoomType(SystemConstants.ROOM_TYPE_GROUP)
                    .setConnectInfo(BeanUtil.toBean(group, GroupVO.class))
                    .setLatestMsg(message)
                    .setUnreadCount((int) unreadCount);
            rooms.add(room);
        });
        return rooms;
    }

    @Override
    public List<RoomVO> getRoomList(Long userId) {
        // 1.先得到用户的好友id和群组id
        List<Long> friendIds = userService.queryFriendIds(userId);
        List<Long> groupIds = userService.queryGroupIds(userId);
        List<RoomVO> friendRooms = getFriendRooms(userId, friendIds);
        List<RoomVO> groupRooms = getGroupRooms(userId, groupIds);

        // 2.将friendRooms和groupRooms合并, 并按照最后一条消息的时间倒序排序
        List<RoomVO> rooms = new ArrayList<>(friendRooms.size() + groupRooms.size());
        rooms.addAll(friendRooms);
        rooms.addAll(groupRooms);
        rooms.sort((o1, o2) -> {
            if (o1.getLatestMsg() == null) {
                return 1;
            }
            if (o2.getLatestMsg() == null) {
                return -1;
            }
            return o2.getLatestMsg().getMessage().getSendTime().compareTo(o1.getLatestMsg().getMessage().getSendTime());
        });
        return rooms;
    }

    @Override
    public void deleteRoom(Long userId, Long roomId) {
//        String key = RedisConstants.USER_DELETE_LATEST_MESSAGE + userId + ":" + roomId;
//        Long roomCurLatestMsgId = Long.valueOf(Objects.requireNonNull(stringRedisTemplate.opsForValue().get(RedisConstants.ROOM_LATEST_MESSAGE + roomId)));
//        stringRedisTemplate.opsForValue().set(key, String.valueOf(roomCurLatestMsgId));
        QueryWrapper<UserRoom> userRoomQueryWrapper = new QueryWrapper<>();
        userRoomQueryWrapper.eq("user_id", userId).eq("room_id", roomId);
        UserRoom userRoom = userRoomMapper.selectList(userRoomQueryWrapper).isEmpty() ? null : userRoomMapper.selectList(userRoomQueryWrapper).get(0);
        ThrowUtils.throwIf(userRoom == null, ErrorStatus.PARAMS_ERROR, "用户房间信息不存在");
        userRoom.setLatestDelMsgId(roomLatestMsgCache.get(roomId));
        userRoomMapper.updateById(userRoom);
        userRoomCache.del(new Pair<>(userId, roomId));
    }
}
