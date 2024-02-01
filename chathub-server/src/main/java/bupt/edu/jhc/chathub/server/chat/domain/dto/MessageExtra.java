package bupt.edu.jhc.chathub.server.chat.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: 消息额外内容
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("消息额外内容")
public class MessageExtra {
    @ApiModelProperty("图片消息")
    private ImgMsgDTO imgMsg; // 图片消息
    @ApiModelProperty("文件消息")
    private FileMsgDTO fileMsg; // 文件消息
}
