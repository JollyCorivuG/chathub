package com.jhc.chathub.service.adapter;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jhc.chathub.common.constants.SystemConstant;
import com.jhc.chathub.common.enums.ErrorStatus;
import com.jhc.chathub.common.exception.ThrowUtils;
import com.jhc.chathub.mapper.GroupRelationMapper;
import com.jhc.chathub.mapper.RoomMapper;
import com.jhc.chathub.pojo.dto.group.CreateGroupDTO;
import com.jhc.chathub.pojo.entity.Group;
import com.jhc.chathub.pojo.entity.GroupRelation;
import com.jhc.chathub.pojo.entity.Room;
import com.jhc.chathub.pojo.vo.GroupVO;
import com.jhc.chathub.pojo.vo.UserVO;
import com.jhc.chathub.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: 群组适配器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/24
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
        Room room = new Room().setRoomType(SystemConstant.ROOM_TYPE_GROUP);
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
        ).toList();
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
