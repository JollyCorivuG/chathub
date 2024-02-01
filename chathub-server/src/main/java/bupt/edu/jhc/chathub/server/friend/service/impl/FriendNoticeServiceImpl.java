package bupt.edu.jhc.chathub.server.friend.service.impl;

import bupt.edu.jhc.chathub.common.domain.vo.resp.Response;
import bupt.edu.jhc.chathub.common.utils.context.RequestHolder;
import bupt.edu.jhc.chathub.server.friend.domain.entity.FriendNotice;
import bupt.edu.jhc.chathub.server.friend.domain.vo.FriendNoticeVO;
import bupt.edu.jhc.chathub.server.friend.mapper.FriendNoticeMapper;
import bupt.edu.jhc.chathub.server.friend.service.IFriendNoticeService;
import bupt.edu.jhc.chathub.server.user.domain.entity.User;
import bupt.edu.jhc.chathub.server.user.domain.vo.UserVO;
import bupt.edu.jhc.chathub.server.user.mapper.UserMapper;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 好友通知服务实现类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Service
public class FriendNoticeServiceImpl extends ServiceImpl<FriendNoticeMapper, FriendNotice> implements IFriendNoticeService {
    @Resource
    private UserMapper userMapper;
    private FriendNoticeVO convertToVo(FriendNotice friendNotice) {
        User user = userMapper.selectById(friendNotice.getOtherUserId());
        FriendNoticeVO friendNoticeVO = new FriendNoticeVO();
        BeanUtil.copyProperties(friendNotice, friendNoticeVO);
        friendNoticeVO.setShowUserInfo(JSONUtil.toBean(JSONUtil.toJsonStr(user), UserVO.class));
        return friendNoticeVO;
    }
    @Override
    public Response<List<FriendNoticeVO>> friendNoticeList() {
        // 1.先查询出来FriendNotice, 按照create_time倒序排列
        Long userId = RequestHolder.get().getUid();
        List<FriendNotice> friendNotices = query().eq("connect_user_id", userId).orderByDesc("create_time").list();

        // 2.将friendNotice转换为friendNoticeVO返回
        return Response.success(friendNotices.stream().map(this::convertToVo).collect(Collectors.toList()));
    }
}
