package bupt.edu.jhc.chathub.server.chat.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: 文本消息 dto
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@Accessors(chain = true)
@ApiModel("文本消息")
public class TextMsgDTO {
    @ApiModelProperty("文本内容")
    private String content;
}
