package bupt.edu.jhc.chathub.common.utils.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 上下文请求信息
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestInfo {
    private Long uid; // 用户id
}
