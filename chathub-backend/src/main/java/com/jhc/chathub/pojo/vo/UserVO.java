package com.jhc.chathub.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class UserVO {
    private Long id;
    private String phone;
    private String account;
    private String nickName;
    private String avatarUrl;
    private Integer level;
    private Integer friendCount;
    private Integer groupCount;
    private Boolean isOnline;
    private Boolean isFriend;
    private LocalDateTime becomeFriendTime;
    private Long roomId;
}
