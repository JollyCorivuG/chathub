package bupt.edu.jhc.chathub.server.sse.service.impl;

import bupt.edu.jhc.chathub.common.domain.enums.ErrorStatus;
import bupt.edu.jhc.chathub.common.utils.exception.BizException;
import bupt.edu.jhc.chathub.common.utils.exception.ThrowUtils;
import bupt.edu.jhc.chathub.server.sse.domain.enums.SseRespType;
import bupt.edu.jhc.chathub.server.sse.domain.vo.SseResponse;
import bupt.edu.jhc.chathub.server.sse.manager.SseSessionManager;
import bupt.edu.jhc.chathub.server.sse.service.ISseService;
import bupt.edu.jhc.chathub.server.user.domain.vo.ForceLogoutInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;

/**
 * @Description: sse 服务实现类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Service
@Slf4j
public class SseServiceImpl implements ISseService {
    private SseEmitter newSseEmitter(Long userId) {
        SseEmitter sseEmitter = new SseEmitter(0L);
        sseEmitter.onError((err)-> {
            log.error("type: SseSession Error, msg: {} session Id : {}",err.getMessage(), userId);
            SseSessionManager.onError(userId, err);
        });
        sseEmitter.onTimeout(() -> {
            log.info("type: SseSession Timeout, session Id : {}", userId);
            SseSessionManager.remove(userId);
        });
        sseEmitter.onCompletion(() -> {
            log.info("type: SseSession Completion, session Id : {}", userId);
            SseSessionManager.remove(userId);
        });
        return sseEmitter;
    }

    @Override
    public SseEmitter connect(Long userId) {
        // 1.先判断是否已经存在
        if (SseSessionManager.isExist(userId)) {
            // 1.1如果已经存在, 则发布一个强制下线的消息
            send(userId, SseResponse.build(SseRespType.FORCE_LOGOUT.getCode(), ForceLogoutInfo.builder().time(LocalDateTime.now()).build()));
            // 1.2然后关闭之前的SseEmitter
            SseSessionManager.remove(userId);
        }

        // 2.创建SseEmitter并加入到SseSessionManager中
        SseEmitter sseEmitter = newSseEmitter(userId);
        log.info("type: SseSession Add, session Id : {}", userId);
        SseSessionManager.add(userId, sseEmitter);

        // 3.返回SseEmitter
        return sseEmitter;
    }

    @Override
    public void close(Long userId) {
        log.info("type: SseSession Close, session Id : {}", userId);
        SseSessionManager.remove(userId);
    }

    @Override
    public void send(Long userId, Object data) {
        ThrowUtils.throwIf(!SseSessionManager.isExist(userId), ErrorStatus.OPERATION_ERROR, "该用户没有SseEmitter");
        try {
            SseSessionManager.send(userId, data);
        } catch (Exception e) {
            throw new BizException(ErrorStatus.OPERATION_ERROR, "发送Sse消息失败");
        }
    }
}
