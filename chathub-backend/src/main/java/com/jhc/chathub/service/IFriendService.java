package com.jhc.chathub.service;

import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.pojo.dto.FriendApplication;
import com.jhc.chathub.pojo.dto.FriendApplicationReply;

public interface IFriendService {
    Response<Void> friendApplication(FriendApplication friendApplication);

    Response<Void> acceptFriendApplication(FriendApplicationReply friendApplicationReply);
}
