package bupt.edu.jhc.chathub.server.trend.domain.dto.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 评论请求
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/22
 */
@Data
@ApiModel("评论请求")
public class PostCommentDTO {
    @ApiModelProperty("说说 id")
    private Long talkId;
    @ApiModelProperty("评论内容")
    private String content;
    @ApiModelProperty("回复的用户 id")
    private Long replyUserId = 0L; // 回复的用户id
}
