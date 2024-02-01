package bupt.edu.jhc.chathub.server.websocket.domain.req;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: ws 请求
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@Accessors(chain = true)
public class WSReqDTO {
    Integer type;
    String data;
}
