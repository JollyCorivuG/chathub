package bupt.edu.jhc.chathub.frequency_control.service;

import bupt.edu.jhc.chathub.frequency_control.domain.FrequencyControlDTO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 限流策略工厂
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/2/1
 */
public class FrequencyControlStrategyFactory {
    /**
     * 指定时间内总次数限流
     */
    public static final String TOTAL_COUNT_WITH_IN_FIX_TIME_FREQUENCY_CONTROLLER = "TotalCountWithInFixTime";

    /**
     * 限流策略集合
     */
    static Map<String, AbstractFrequencyControlService<?>> frequencyControlServiceStrategyMap = new ConcurrentHashMap<>(8);

    public static <K extends FrequencyControlDTO> void registerFrequencyController(String strategyName, AbstractFrequencyControlService<K> abstractFrequencyControlService) {
        frequencyControlServiceStrategyMap.put(strategyName, abstractFrequencyControlService);
    }

    @SuppressWarnings("unchecked")
    public static <K extends FrequencyControlDTO> AbstractFrequencyControlService<K> getFrequencyControllerByName(String strategyName) {
        return (AbstractFrequencyControlService<K>) frequencyControlServiceStrategyMap.get(strategyName);
    }
}
