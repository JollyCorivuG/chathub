package bupt.edu.jhc.chathub.server.group.service;

import bupt.edu.jhc.chathub.server.group.domain.dto.CreateGroupDTO;
import bupt.edu.jhc.chathub.server.group.domain.dto.InvitationRespDTO;
import bupt.edu.jhc.chathub.server.group.domain.entity.Group;
import bupt.edu.jhc.chathub.server.group.domain.vo.GroupVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 群组服务接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
public interface IGroupService extends IService<Group> {
    GroupVO createGroup(Long userId, CreateGroupDTO createGroup);

    List<GroupVO> getManageGroup(Long userId);

    List<GroupVO> getJoinGroup(Long userId);

    GroupVO getGroupInfo(Long userId, Long groupId);

    void inviteUsers(Long userId, List<Long> inviteUserIds, Long groupId);

    void invitationResp(Long userId, InvitationRespDTO invitationResp);
}
