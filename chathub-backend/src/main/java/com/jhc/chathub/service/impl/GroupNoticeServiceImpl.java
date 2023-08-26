package com.jhc.chathub.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.chathub.mapper.GroupNoticeMapper;
import com.jhc.chathub.pojo.entity.GroupNotice;
import com.jhc.chathub.pojo.vo.GroupNoticeVO;
import com.jhc.chathub.service.IGroupNoticeService;
import com.jhc.chathub.service.adapter.GroupNoticeAdapter;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 群组通知服务实现类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/25
 */
@Service
public class GroupNoticeServiceImpl extends ServiceImpl<GroupNoticeMapper, GroupNotice> implements IGroupNoticeService {
    @Resource
    private GroupNoticeAdapter groupNoticeAdapter;

    @Override
    public List<GroupNoticeVO> getGroupNotice(Long userId) {
        // 1.根据 userId 查询 GroupNotice
        List<GroupNotice> groupNotices = query().eq("user_id", userId).orderByDesc("create_time").list();

        // 2.将 GroupNotice 转换为 GroupNoticeVO
        return groupNotices.stream().map(groupNoticeAdapter::buildGroupNoticeResp).toList();
    }
}
