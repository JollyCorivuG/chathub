package com.jhc.chathub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.pojo.dto.friend.FriendApplication;
import com.jhc.chathub.pojo.dto.friend.FriendApplicationReply;
import com.jhc.chathub.pojo.entity.FriendRelation;

public interface IFriendService extends IService<FriendRelation> {
    Response<Void> friendApplication(FriendApplication friendApplication);

    Response<Void> acceptFriendApplication(FriendApplicationReply friendApplicationReply);

    Response<Void> deleteFriend(Long id);

    Long getRoomId(Long selfId, Long otherId);
}
