package bupt.edu.jhc.chathub.server.group.service.adapter;

import bupt.edu.jhc.chathub.server.group.domain.entity.GroupNotice;
import bupt.edu.jhc.chathub.server.group.domain.vo.GroupNoticeVO;
import bupt.edu.jhc.chathub.server.group.domain.vo.GroupVO;
import bupt.edu.jhc.chathub.server.group.mapper.GroupMapper;
import cn.hutool.core.bean.BeanUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description: 群组通知适配器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
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

