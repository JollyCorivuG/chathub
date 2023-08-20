package com.jhc.chathub.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForceLogoutInfo {
    /**
     * 被强制下线的时间
     */
    LocalDateTime time;
}
