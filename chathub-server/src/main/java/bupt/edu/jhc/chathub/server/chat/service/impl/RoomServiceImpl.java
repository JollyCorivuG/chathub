package bupt.edu.jhc.chathub.server.chat.service.impl;

import bupt.edu.jhc.chathub.server.chat.domain.entity.Room;
import bupt.edu.jhc.chathub.server.chat.mapper.RoomMapper;
import bupt.edu.jhc.chathub.server.chat.service.IRoomService;
import bupt.edu.jhc.chathub.server.friend.domain.entity.FriendRelation;
import bupt.edu.jhc.chathub.server.friend.service.IFriendService;
import bupt.edu.jhc.chathub.server.group.domain.entity.Group;
import bupt.edu.jhc.chathub.server.group.domain.entity.GroupRelation;
import bupt.edu.jhc.chathub.server.group.mapper.GroupRelationMapper;
import bupt.edu.jhc.chathub.server.group.service.IGroupService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 房间服务实现类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements IRoomService {

    @Resource
    private IFriendService friendService;

    @Resource
    private IGroupService groupService;

    @Resource
    private GroupRelationMapper groupRelationMapper;

    @Override
    public List<Long> listUserIdsByRoomId(Long roomId) {
        List<Long> results = new ArrayList<>();

        // 1.先查询好友关系中的用户
        List<FriendRelation> friendRelationList = friendService.query().eq("room_id", roomId).list();
        friendRelationList.forEach(friendRelation -> {
            results.add(friendRelation.getUserId1());
            results.add(friendRelation.getUserId2());
        });

        // 2.再查询群组中的用户
        List<Group> groups = groupService.query().eq("room_id", roomId).list();
        groups.forEach(group -> {
            QueryWrapper<GroupRelation> wrapper = new QueryWrapper<GroupRelation>().eq("group_id", group.getId());
            List<GroupRelation> groupRelations = groupRelationMapper.selectList(wrapper);
            groupRelations.forEach(groupRelation -> {
                results.add(groupRelation.getUserId());
            });
            results.add(group.getOwnerId());
        });

        // 3.返回结果
        return results;
    }
}
