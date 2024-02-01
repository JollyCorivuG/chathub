package bupt.edu.jhc.chathub.server.group.service.impl;

import bupt.edu.jhc.chathub.server.group.domain.entity.GroupNotice;
import bupt.edu.jhc.chathub.server.group.domain.vo.GroupNoticeVO;
import bupt.edu.jhc.chathub.server.group.mapper.GroupNoticeMapper;
import bupt.edu.jhc.chathub.server.group.service.IGroupNoticeService;
import bupt.edu.jhc.chathub.server.group.service.adapter.GroupNoticeAdapter;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 群组通知接口实现
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
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
        return groupNotices.stream().map(groupNoticeAdapter::buildGroupNoticeResp).collect(Collectors.toList());
    }
}
