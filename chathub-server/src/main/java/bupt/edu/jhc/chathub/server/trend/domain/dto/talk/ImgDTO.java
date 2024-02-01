package bupt.edu.jhc.chathub.server.trend.domain.dto.talk;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: 图片 dto
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/22
 */
@Data
@Accessors(chain = true)
@ApiModel("说说图片信息")
public class ImgDTO {
    @ApiModelProperty("图片 url")
    private String url;
}
