package com.jhc.chathub.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.jhc.chathub.mapper.GroupMapper;
import com.jhc.chathub.pojo.entity.GroupNotice;
import com.jhc.chathub.pojo.vo.GroupNoticeVO;
import com.jhc.chathub.pojo.vo.GroupVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @Description: 群组通知适配器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/25
 */
@Component
public class GroupNoticeAdapter {
    @Resource
    private GroupMapper groupMapper;

    public GroupNoticeVO buildGroupNoticeResp(GroupNotice groupNotice) {
        GroupVO groupInfo = BeanUtil.toBean(groupMapper.selectById(groupNotice.getGroupId()), GroupVO.class);
        return GroupNoticeVO.builder()
                .id(groupNotice.getId())
                .showGroupInfo(groupInfo)
                .description(groupNotice.getDescription())
                .statusInfo(groupNotice.getStatusInfo())
                .createTime(groupNotice.getCreateTime())
                .build();
    }
}
