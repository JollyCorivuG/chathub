package bupt.edu.jhc.chathub.server.trend.domain.vo;

import bupt.edu.jhc.chathub.server.trend.domain.dto.talk.TalkExtra;
import bupt.edu.jhc.chathub.server.user.domain.vo.UserVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @Description: 说说 vo
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/22
 */
@Data
@Accessors(chain = true)
@ApiModel("说说信息")
public class TalkVO {
    @ApiModelProperty("说说 id")
    private Long id;
    @ApiModelProperty("说说作者信息")
    private UserVO author;
    @ApiModelProperty("说说内容")
    private String content;
    @ApiModelProperty("说说附加内容, 支持图片")
    private List<TalkExtra> extra;
    @ApiModelProperty("点赞数")
    private Integer likeCount;
    @ApiModelProperty("是否点赞")
    private Boolean isLike;
    @ApiModelProperty("最新点赞的用户, 最多显示5个")
    private List<UserVO> latestLikeUsers;
    @ApiModelProperty("评论列表")
    private List<CommentVO> comments = Collections.emptyList();
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
