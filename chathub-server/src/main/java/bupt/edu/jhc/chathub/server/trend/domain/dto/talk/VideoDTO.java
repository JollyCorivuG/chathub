package bupt.edu.jhc.chathub.server.trend.domain.dto.talk;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: 视频 dto
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/22
 */
@Data
@Accessors(chain = true)
@ApiModel("说说视频信息")
public class VideoDTO {
    @ApiModelProperty("视频 url")
    private String url;
    @ApiModelProperty("视频封面 url")
    private String coverUrl;
}
