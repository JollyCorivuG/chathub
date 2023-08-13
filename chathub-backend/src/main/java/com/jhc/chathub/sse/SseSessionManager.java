package com.jhc.chathub.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/13
 */
public class SseSessionManager {
    // userId -> SseEmitter
    private static final ConcurrentHashMap<Long, SseEmitter> SESSION_MAP = new ConcurrentHashMap<>();

    // 添加SseEmitter
    public static void add(Long userId, SseEmitter sseEmitter) {
        if (isExist(userId)) {
            throw new RuntimeException("该用户已经有一个SseEmitter了");
        }
        SESSION_MAP.put(userId, sseEmitter);
    }

    // 判断是否存在SseEmitter
    public static boolean isExist(Long userId) {
        return SESSION_MAP.get(userId) != null;
    }

    // 移除SseEmitter
    public static void remove(Long userId) {
        SseEmitter sseEmitter = SESSION_MAP.get(userId);
        if (sseEmitter == null) {
            throw new RuntimeException("该用户没有SseEmitter");
        }
        sseEmitter.complete();
        SESSION_MAP.remove(userId);
    }

    // 发生错误
    public static void onError(Long userId, Throwable throwable) {
        SseEmitter sseEmitter = SESSION_MAP.get(userId);
        if (sseEmitter == null) {
            throw new RuntimeException("该用户没有SseEmitter");
        }
        sseEmitter.completeWithError(throwable);
    }

    // 发送消息
    public static void send(Long userId, Object data) throws IOException {
        SseEmitter sseEmitter = SESSION_MAP.get(userId);
        if (sseEmitter == null) {
            throw new RuntimeException("该用户没有SseEmitter");
        }
        sseEmitter.send(data);
    }
}
