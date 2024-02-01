package bupt.edu.jhc.chathub.server.trend.domain.dto.talk;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Description: 创建说说请求
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/22
 */
@Data
@Accessors(chain = true)
@ApiModel("创建说说请求")
public class CreateTalkDTO {
    @ApiModelProperty("说说内容")
    private String content;
    @ApiModelProperty("说说附加内容, 支持图片")
    private List<TalkExtra> extra;
}
