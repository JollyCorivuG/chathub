package com.jhc.chathub.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.mapper.FriendNoticeMapper;
import com.jhc.chathub.mapper.UserMapper;
import com.jhc.chathub.pojo.entity.FriendNotice;
import com.jhc.chathub.pojo.entity.User;
import com.jhc.chathub.pojo.vo.FriendNoticeVO;
import com.jhc.chathub.pojo.vo.UserVO;
import com.jhc.chathub.service.IFriendNoticeService;
import com.jhc.chathub.utils.UserHolder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
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
        Long userId = UserHolder.getUser().getId();
        List<FriendNotice> friendNotices = query().eq("connect_user_id", userId).orderByDesc("create_time").list();

        // 2.将friendNotice转换为friendNoticeVO返回
        return Response.success(friendNotices.stream().map(this::convertToVo).toList());
    }
}
