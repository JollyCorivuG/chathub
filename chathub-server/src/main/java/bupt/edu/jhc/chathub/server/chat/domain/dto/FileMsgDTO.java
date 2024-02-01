package bupt.edu.jhc.chathub.server.chat.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: 文件消息 dto
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@Accessors(chain = true)
@ApiModel("文件消息")
public class FileMsgDTO {
    @ApiModelProperty("文件大小")
    private Long size;
    @ApiModelProperty("文件类型")
    private String url;
    @ApiModelProperty("文件名")
    private String fileName;
}
