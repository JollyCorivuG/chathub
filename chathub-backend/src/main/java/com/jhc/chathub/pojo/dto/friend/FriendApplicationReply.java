package com.jhc.chathub.pojo.dto.friend;


import lombok.Data;

@Data
public class FriendApplicationReply {
    private Boolean isAccept;
    private Long noticeId;
    private Long userId;
}
