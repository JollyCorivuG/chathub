package bupt.edu.jhc.chathub.server.trend.domain.dto.talk;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: 点赞信息
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/22
 */
@Data
@Accessors(chain = true)
@ApiModel("点赞信息")
public class LikeInfoDTO {
    @ApiModelProperty("说说 id")
    private Long talkId;
    @ApiModelProperty("点赞 or 取消点赞")
    private Boolean isLike;
}
