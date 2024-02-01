package bupt.edu.jhc.chathub.server.group.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 入群邀请响应信息
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@ApiModel("入群邀请响应信息")
public class InvitationRespDTO {
    @ApiModelProperty("群组id")
    private Long groupId;
    @ApiModelProperty("通知id")
    private Long noticeId;
    @ApiModelProperty("是否同意")
    private Boolean isAgree;
}
