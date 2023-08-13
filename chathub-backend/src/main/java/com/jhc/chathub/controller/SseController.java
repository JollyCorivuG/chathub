package com.jhc.chathub.controller;

import com.jhc.chathub.service.ISseService;
import com.jhc.chathub.utils.UserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/13
 */
@RestController
@RequestMapping("/sse")
@Tag(name = "SSE相关接口")
@CrossOrigin
public class SseController {
    @Resource
    private ISseService sseService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "订阅SSE")
    public SseEmitter subscribe() {
        Long userId = UserHolder.getUser().getId();
        return sseService.connect(userId);
    }

    @GetMapping(value = "/close")
    @Operation(summary = "关闭SSE")
    public void close(){
        Long userId = UserHolder.getUser().getId();
        sseService.close(userId);
    }
}
