package bupt.edu.jhc.chathub.server.chat.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: 图片消息 dto
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@Accessors(chain = true)
@ApiModel("图片消息")
public class ImgMsgDTO {
    @ApiModelProperty("图片大小")
    private Long size;
    @ApiModelProperty("图片宽度")
    private Integer width;
    @ApiModelProperty("图片高度")
    private Integer height;
    @ApiModelProperty("图片地址")
    private String url;
}