package bupt.edu.jhc.chathub.server.sse.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @Description: sse 服务接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
public interface ISseService {
    SseEmitter connect(Long userId);

    void close(Long userId);

    void send(Long userId, Object data);
}
