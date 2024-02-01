package bupt.edu.jhc.chathub.server.group.service;

import bupt.edu.jhc.chathub.server.group.domain.entity.GroupNotice;
import bupt.edu.jhc.chathub.server.group.domain.vo.GroupNoticeVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 群组通知服务接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
public interface IGroupNoticeService extends IService<GroupNotice> {
    List<GroupNoticeVO> getGroupNotice(Long userId);
}
