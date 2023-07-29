package com.jhc.chathub.pojo.dto.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserDTO {
    private Long id;
    private String account;
    private String nickName;
    private String avatarUrl;
    private Integer level;
    private Integer friendCount;
    private Integer groupCount;
}
