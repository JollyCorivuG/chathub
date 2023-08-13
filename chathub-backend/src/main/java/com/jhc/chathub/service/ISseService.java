package com.jhc.chathub.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/13
 */
public interface ISseService {
    SseEmitter connect(Long userId);

    void close(Long userId);

    void send(Long userId, Object data);
}
