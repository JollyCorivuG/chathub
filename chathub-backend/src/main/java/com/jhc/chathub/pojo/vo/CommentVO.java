package com.jhc.chathub.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/11
 */
@Data
@Accessors(chain = true)
public class CommentVO {
    private Long id;
    private UserVO sender;
    private String content;
    private Long replyUserId; // 回复的用户id
    private UserVO replyUser; // 回复的用户信息
    private LocalDateTime createTime;
}
