package com.jhc.chathub.pojo.dto;


import lombok.Data;

@Data
public class FriendApplicationReply {
    private Boolean isAccept;
    private Long noticeId;
    private Long userId;
}
