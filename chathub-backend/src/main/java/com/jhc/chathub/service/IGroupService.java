package com.jhc.chathub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.chathub.pojo.dto.group.CreateGroupDTO;
import com.jhc.chathub.pojo.dto.group.InvitationRespDTO;
import com.jhc.chathub.pojo.entity.Group;
import com.jhc.chathub.pojo.vo.GroupVO;

import java.util.List;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/24
 */
public interface IGroupService extends IService<Group> {
    GroupVO createGroup(Long userId, CreateGroupDTO createGroup);

    List<GroupVO> getManageGroup(Long userId);

    List<GroupVO> getJoinGroup(Long userId);

    GroupVO getGroupInfo(Long userId, Long groupId);

    void inviteUsers(Long userId, List<Long> inviteUserIds, Long groupId);

    void invitationResp(Long userId, InvitationRespDTO invitationResp);
}
