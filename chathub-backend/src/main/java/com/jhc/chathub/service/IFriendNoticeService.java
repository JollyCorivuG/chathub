package com.jhc.chathub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.pojo.entity.FriendNotice;
import com.jhc.chathub.pojo.vo.FriendNoticeVO;

import java.util.List;

public interface IFriendNoticeService extends IService<FriendNotice> {
    Response<List<FriendNoticeVO>> friendNoticeList();
}
