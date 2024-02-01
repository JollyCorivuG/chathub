package bupt.edu.jhc.chathub.server.user.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Description: 强制下线信息
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("强制下线信息")
public class ForceLogoutInfo {
    /**
     * 被强制下线的时间
     */
    @ApiModelProperty("被强制下线的时间")
    LocalDateTime time;
}
