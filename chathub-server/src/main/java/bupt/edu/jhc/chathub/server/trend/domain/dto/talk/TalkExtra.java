package bupt.edu.jhc.chathub.server.trend.domain.dto.talk;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: 说说额外内容
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/22
 */
@Data
@Accessors(chain = true)
@ApiModel("说说额外内容")
public class TalkExtra {
    @ApiModelProperty("类型")
    private Integer type;
    @ApiModelProperty("数据")
    private Object data;
}
