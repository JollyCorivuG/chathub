package com.jhc.chathub.pojo.vo;

import com.jhc.chathub.pojo.dto.talk.TalkExtra;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/11
 */
@Data
@Accessors(chain = true)
public class TalkVO {
    private Long id;
    private UserVO author;
    private String content;
    private List<TalkExtra> extra;
    private Integer likeCount;
    private List<UserVO> latestLikeUsers; // 最新点赞的用户, 最多显示5个
    private List<CommentVO> comments = Collections.emptyList();
    private LocalDateTime createTime;
}
