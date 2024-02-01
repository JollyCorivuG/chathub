package bupt.edu.jhc.chathub.server.user.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: 登录表单
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@Accessors(chain = true)
@ApiModel("登录表单")
public class LoginFormDTO {
    @ApiModelProperty("账号")
    private String account;
    @ApiModelProperty("密码")
    private String password;
}
