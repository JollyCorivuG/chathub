package com.jhc.chathub.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class FriendNoticeVO {
    private Long id;
    private UserVO showUserInfo;
    private String description;
    private Integer noticeType;
    private Integer statusInfo;
    private LocalDateTime createTime;
}
