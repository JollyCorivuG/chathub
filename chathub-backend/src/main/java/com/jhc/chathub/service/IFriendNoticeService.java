package com.jhc.chathub.service;

import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.pojo.vo.FriendNoticeVO;

import java.util.List;

public interface IFriendNoticeService {
    Response<List<FriendNoticeVO>> friendNoticeList();
}
