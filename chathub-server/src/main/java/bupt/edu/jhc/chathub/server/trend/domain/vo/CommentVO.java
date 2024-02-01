package bupt.edu.jhc.chathub.server.trend.domain.vo;

import bupt.edu.jhc.chathub.server.user.domain.vo.UserVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Description: 评论 vo
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/22
 */
@Data
@Accessors(chain = true)
@ApiModel("评论信息")
public class CommentVO {
    @ApiModelProperty("评论 id")
    private Long id;
    @ApiModelProperty("说说 id")
    private UserVO sender;
    @ApiModelProperty("评论内容")
    private String content;
    @ApiModelProperty("回复的评论 id")
    private Long replyUserId;
    @ApiModelProperty("回复的用户信息")
    private UserVO replyUser;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
