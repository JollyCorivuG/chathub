package bupt.edu.jhc.chathub.server.group.service.adapter;

import bupt.edu.jhc.chathub.common.domain.constants.SystemConstants;
import bupt.edu.jhc.chathub.common.domain.enums.ErrorStatus;
import bupt.edu.jhc.chathub.common.utils.exception.ThrowUtils;
import bupt.edu.jhc.chathub.server.chat.domain.entity.Room;
import bupt.edu.jhc.chathub.server.chat.mapper.RoomMapper;
import bupt.edu.jhc.chathub.server.group.domain.dto.CreateGroupDTO;
import bupt.edu.jhc.chathub.server.group.domain.entity.Group;
import bupt.edu.jhc.chathub.server.group.domain.entity.GroupRelation;
import bupt.edu.jhc.chathub.server.group.domain.vo.GroupVO;
import bupt.edu.jhc.chathub.server.group.mapper.GroupRelationMapper;
import bupt.edu.jhc.chathub.server.user.domain.vo.UserVO;
import bupt.edu.jhc.chathub.server.user.service.IUserService;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 群组适配器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Component
public class GroupAdapter {
    @Resource
    private RoomMapper roomMapper;

    @Resource
    private IUserService userService;

    @Resource
    private GroupRelationMapper groupRelationMapper;


    public Group buildGroupSave(Long userId, CreateGroupDTO createGroup) {
        ThrowUtils.throwIf(createGroup == null, ErrorStatus.PARAMS_ERROR);
        String groupNumber = RandomUtil.randomNumbers(6);
        Room room = new Room().setRoomType(SystemConstants.ROOM_TYPE_GROUP);
        roomMapper.insert(room);
        return Group.builder()
                .number(groupNumber)
                .name(createGroup.getName())
                .avatar(createGroup.getAvatar())
                .peopleNum(1)
                .maxPeopleNum(createGroup.getMaxPeopleNum())
                .ownerId(userId)
                .roomId(room.getId())
                .build();
    }

    public GroupVO buildGroupResp(Long userId, Group group) {
        ThrowUtils.throwIf(group == null, ErrorStatus.NOT_FOUND_ERROR);
        UserVO owner = userService.convertUserToUserVO(userId, userService.getById(group.getOwnerId()));
        QueryWrapper<GroupRelation> queryWrapper = new QueryWrapper<GroupRelation>().eq("group_id", group.getId());
        List<GroupRelation> groupRelations = groupRelationMapper.selectList(queryWrapper);
        List<UserVO> members = groupRelations.stream().map(
                relation -> userService.convertUserToUserVO(userId, userService.getById(relation.getUserId()))
        ).collect(Collectors.toList());
        return GroupVO.builder()
                .id(group.getId())
                .number(group.getNumber())
                .name(group.getName())
                .avatar(group.getAvatar())
                .peopleNum(group.getPeopleNum())
                .owner(owner)
                .members(members)
                .roomId(group.getRoomId())
                .createTime(group.getCreateTime())
                .build();
    }
}
