package com.jhc.chathub.pojo.dto.group;

import lombok.Data;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/25
 */
@Data
public class InvitationRespDTO {
    private Long groupId;
    private Long noticeId;
    private Boolean isAgree;
}
