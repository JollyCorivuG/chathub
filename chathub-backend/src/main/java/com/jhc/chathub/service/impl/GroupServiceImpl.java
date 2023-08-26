package com.jhc.chathub.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.chathub.common.constants.SystemConstant;
import com.jhc.chathub.common.enums.ErrorStatus;
import com.jhc.chathub.common.exception.ThrowUtils;
import com.jhc.chathub.mapper.GroupMapper;
import com.jhc.chathub.mapper.GroupRelationMapper;
import com.jhc.chathub.pojo.dto.group.CreateGroupDTO;
import com.jhc.chathub.pojo.dto.group.InvitationRespDTO;
import com.jhc.chathub.pojo.entity.Group;
import com.jhc.chathub.pojo.entity.GroupNotice;
import com.jhc.chathub.pojo.entity.GroupRelation;
import com.jhc.chathub.pojo.vo.GroupVO;
import com.jhc.chathub.service.IGroupNoticeService;
import com.jhc.chathub.service.IGroupService;
import com.jhc.chathub.service.adapter.GroupAdapter;
import com.jhc.chathub.utils.UserHolder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/24
 */
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements IGroupService {
    @Resource
    private GroupAdapter groupAdapter;

    @Resource
    private GroupRelationMapper groupRelationMapper;

    @Resource
    private IGroupNoticeService groupNoticeService;

    @Override
    public GroupVO createGroup(Long userId, CreateGroupDTO createGroup) {
        // 1.构建群组信息, 保存到数据库
        Group group = groupAdapter.buildGroupSave(userId, createGroup);
        save(group);

        // 2.返回群组信息
        return groupAdapter.buildGroupResp(userId, group);
    }

    @Override
    public List<GroupVO> getManageGroup(Long userId) {
        // 1.根据 userId 查询管理的群组
        List<Group> groups = query().eq("owner_id", userId).list();

        // 2.将群组信息转换为群组响应信息返回 (这里不需要查询群组成员信息)
        return groups.stream().map(group -> BeanUtil.toBean(group, GroupVO.class)).toList();
    }

    @Override
    public List<GroupVO> getJoinGroup(Long userId) {
        // 1.根据 userId 查询加入的群组 id 集合
        QueryWrapper<GroupRelation> wrapper = new QueryWrapper<GroupRelation>().eq("user_id", userId);
        List<GroupRelation> groupRelations = groupRelationMapper.selectList(wrapper);

        // 2.根据群组 id 集合查询群组信息并转换为群组响应信息返回
        return groupRelations.stream().map(groupRelation -> {
            Group group = getById(groupRelation.getGroupId());
            return BeanUtil.toBean(group, GroupVO.class);
        }).toList();
    }

    @Override
    public GroupVO getGroupInfo(Long userId, Long groupId) {
        return groupAdapter.buildGroupResp(userId, getById(groupId));
    }

    @Override
    public void inviteUsers(Long userId, List<Long> inviteUserIds, Long groupId) {
        // 1.先得到邀请的描述信息
        String name = UserHolder.getUser().getNickName();
        Group group = getById(groupId);
        String content = String.format("用户 %s 邀请你加入群组 %s", name, group.getName());

        // 2.创建通知
        List<GroupNotice> notices = inviteUserIds.stream().map(id -> GroupNotice.builder()
                .userId(id)
                .groupId(groupId)
                .description(content)
                .statusInfo(SystemConstant.GROUP_NOTICE_STATUS_PENDING)
                .build()).toList();

        // 3.插入通知
        groupNoticeService.saveBatch(notices);
    }

    @Override
    @Transactional
    public void invitationResp(Long userId, InvitationRespDTO invitationResp) {
        // 1.判断是同意还是拒绝
        if (!invitationResp.getIsAgree()) {
            // 1.1如果是拒绝, 那直接更新通知状态
            groupNoticeService.update().setSql(String.format("status_info = %d", SystemConstant.GROUP_NOTICE_STATUS_REFUSE)).eq("id", invitationResp.getNoticeId()).update();
            return;
        }

        // 2.如果是同意,则需要判断判断群人数是否已经达到上限
        Group group = getById(invitationResp.getGroupId());
        ThrowUtils.throwIf(Objects.equals(group.getPeopleNum(), group.getMaxPeopleNum()), ErrorStatus.OPERATION_ERROR, "群人数已达上限");

        // 3.判断可以加入后, 先更新群组人数
        boolean isSuccess = update().setSql("people_num = people_num + 1")
                .eq("id", invitationResp.getGroupId())
                .lt("people_num", group.getMaxPeopleNum())
                .update();
        ThrowUtils.throwIf(!isSuccess, ErrorStatus.OPERATION_ERROR, "群人数已达上限");

        // 4.更新通知
        groupNoticeService.update().setSql(String.format("status_info = %d", SystemConstant.GROUP_NOTICE_STATUS_AGREE)).eq("id", invitationResp.getNoticeId()).update();

        // 5.插入群组关系
        groupRelationMapper.insert(GroupRelation.builder()
                .userId(userId)
                .groupId(invitationResp.getGroupId())
                .build());
    }
}
