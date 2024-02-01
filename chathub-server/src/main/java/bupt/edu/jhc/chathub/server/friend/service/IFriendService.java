package bupt.edu.jhc.chathub.server.friend.service;

import bupt.edu.jhc.chathub.common.domain.vo.resp.Response;
import bupt.edu.jhc.chathub.server.friend.domain.dto.FriendApplication;
import bupt.edu.jhc.chathub.server.friend.domain.dto.FriendApplicationReply;
import bupt.edu.jhc.chathub.server.friend.domain.entity.FriendRelation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 好友服务接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
public interface IFriendService extends IService<FriendRelation> {
    Response<?> friendApplication(FriendApplication friendApplication);

    Response<?> acceptFriendApplication(FriendApplicationReply friendApplicationReply);

    Response<?> deleteFriend(Long id);

    Long getRoomId(Long selfId, Long otherId);
}
