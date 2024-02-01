package bupt.edu.jhc.chathub.server.friend.service;

import bupt.edu.jhc.chathub.common.domain.vo.resp.Response;
import bupt.edu.jhc.chathub.server.friend.domain.entity.FriendNotice;
import bupt.edu.jhc.chathub.server.friend.domain.vo.FriendNoticeVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 好友通知服务接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
public interface IFriendNoticeService extends IService<FriendNotice> {
    Response<List<FriendNoticeVO>> friendNoticeList();
}
