package com.jhc.chathub.pojo.dto.comment;

import lombok.Data;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/11
 */
@Data
public class PostCommentDTO {
    private Long talkId;
    private String content;
    private Long replyUserId = 0L; // 回复的用户id
}
