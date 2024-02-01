package bupt.edu.jhc.chathub.frequency_control.aspect;

import bupt.edu.jhc.chathub.common.utils.context.RequestHolder;
import bupt.edu.jhc.chathub.frequency_control.annotation.FrequencyControl;
import bupt.edu.jhc.chathub.frequency_control.domain.FrequencyControlDTO;
import bupt.edu.jhc.chathub.frequency_control.utils.FrequencyControlUtils;
import bupt.edu.jhc.chathub.frequency_control.utils.SpElUtils;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static bupt.edu.jhc.chathub.frequency_control.service.FrequencyControlStrategyFactory.TOTAL_COUNT_WITH_IN_FIX_TIME_FREQUENCY_CONTROLLER;

/**
 * @Description: 频控切面
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/2/1
 */
@Aspect
@Component
@Slf4j
public class FrequencyControlAspect {
    /**
     * @Description: 频控切面
     * @param key
     * @param frequencyControl
     * @return
     */
    private FrequencyControlDTO buildFrequencyControlDTO(String key, FrequencyControl frequencyControl) {
        return FrequencyControlDTO.builder()
                .key(key)
                .time(frequencyControl.time())
                .unit(frequencyControl.unit())
                .count(frequencyControl.count())
                .build();
    }

    /**
     * @Description: 频控切面
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(bupt.edu.jhc.chathub.frequency_control.annotation.FrequencyControl) || @annotation(bupt.edu.jhc.chathub.frequency_control.annotation.FrequencyControlContainer)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1.获取方法上的注解
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        FrequencyControl[] annotationsByType = method.getAnnotationsByType(FrequencyControl.class);

        // 2.构建频控的 key
        Map<String, FrequencyControl> keyMap = new HashMap<>();
        for (int i = 0; i < annotationsByType.length; i++) {
            FrequencyControl frequencyControl = annotationsByType[i];
            String prefix = StrUtil.isBlank(frequencyControl.prefixKey()) ? SpElUtils.getMethodKey(method) + ":index:" + i : frequencyControl.prefixKey(); // 默认方法限定名 + 注解排名（可能多个）
            String key = "";
            switch (frequencyControl.target()) {
                case EL:
                    key = SpElUtils.parseSpEl(method, joinPoint.getArgs(), frequencyControl.spEl());
                    break;
                case UID:
                    key = RequestHolder.get().getUid().toString();
            };
            keyMap.put(prefix + ":" + key, frequencyControl);
        }

        // 3.将注解的参数转换为编程式调用需要的参数
        List<FrequencyControlDTO> frequencyControlDTOS = keyMap.entrySet().stream().map(entrySet -> buildFrequencyControlDTO(entrySet.getKey(), entrySet.getValue())).collect(Collectors.toList());
        return FrequencyControlUtils.executeWithFrequencyControlList(TOTAL_COUNT_WITH_IN_FIX_TIME_FREQUENCY_CONTROLLER, frequencyControlDTOS, joinPoint::proceed);
    }
}
