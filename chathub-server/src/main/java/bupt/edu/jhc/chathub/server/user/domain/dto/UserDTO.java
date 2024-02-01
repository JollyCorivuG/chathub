package bupt.edu.jhc.chathub.server.user.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: 用户 dto
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@Accessors(chain = true)
public class UserDTO {
    private Long id;
    private String account;
    private String nickName;
    private String avatarUrl;
    private Integer level;
    private Integer friendCount;
    private Integer groupCount;
}
