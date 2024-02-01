package bupt.edu.jhc.chathub.server.group.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 创建群组 dto
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@ApiModel("创建群组请求信息")
public class CreateGroupDTO {
    @ApiModelProperty("群组名称")
    private String name;
    @ApiModelProperty("群组头像")
    private String avatar;
    @ApiModelProperty("群组最大人数")
    private Integer maxPeopleNum;
}
