package bupt.edu.jhc.chathub.server.sse.controller;

import bupt.edu.jhc.chathub.common.utils.context.RequestHolder;
import bupt.edu.jhc.chathub.server.sse.service.ISseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;

/**
 * @Description: sse 接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@RestController
@RequestMapping("/api/sse")
@Api(tags = "SSE相关接口")
@CrossOrigin
public class SseController {
    @Resource
    private ISseService sseService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ApiOperation("订阅SSE")
    public SseEmitter subscribe() {
        Long userId = RequestHolder.get().getUid();
        return sseService.connect(userId);
    }

    @GetMapping(value = "/close")
    @ApiOperation("关闭SSE")
    public void close(){
        Long userId = RequestHolder.get().getUid();
        sseService.close(userId);
    }

    @PostMapping(value = "/unsubscribe")
    @ApiOperation("取消订阅SSE")
    public void unsubscribe(@RequestParam("userId") Long userId) {
        sseService.close(userId);
    }
}