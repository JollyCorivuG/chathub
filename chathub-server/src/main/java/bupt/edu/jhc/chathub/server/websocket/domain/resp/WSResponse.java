package bupt.edu.jhc.chathub.server.websocket.domain.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: ws 响应
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@Accessors(chain = true)
public class WSResponse<T> {
    Integer type;
    T data;
    public static <T> WSResponse<T> build(Integer type, T data) {
        WSResponse<T> response = new WSResponse<>();
        return response.setType(type).setData(data);
    }
}
