package bupt.edu.jhc.chathub.server.chat.service.strategy;


import bupt.edu.jhc.chathub.common.domain.enums.ErrorStatus;
import bupt.edu.jhc.chathub.common.utils.exception.ThrowUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 消息处理器工厂类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/23
 */
@Slf4j
public class MsgHandlerFactory {
    private static final Map<Integer, AbstractMsgHandler> STRATEGY_MAP = new HashMap<>();

    public static void register(Integer code, AbstractMsgHandler strategy) {
        STRATEGY_MAP.put(code, strategy);
    }

    public static AbstractMsgHandler getStrategy(Integer code) {
        AbstractMsgHandler strategy = STRATEGY_MAP.get(code);
        ThrowUtils.throwIf(strategy == null, ErrorStatus.PARAMS_ERROR, "消息类型错误");
        return strategy;
    }
}
