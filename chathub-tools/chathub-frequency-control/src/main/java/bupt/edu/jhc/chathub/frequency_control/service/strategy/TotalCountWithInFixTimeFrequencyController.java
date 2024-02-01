package bupt.edu.jhc.chathub.frequency_control.service.strategy;

import bupt.edu.jhc.chathub.frequency_control.domain.FrequencyControlDTO;
import bupt.edu.jhc.chathub.frequency_control.service.AbstractFrequencyControlService;
import bupt.edu.jhc.chathub.frequency_control.utils.RedisUtils2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static bupt.edu.jhc.chathub.frequency_control.service.FrequencyControlStrategyFactory.TOTAL_COUNT_WITH_IN_FIX_TIME_FREQUENCY_CONTROLLER;

/**
 * @Description: 固定时间内不超过固定次数的限流类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/2/1
 */
@Slf4j
@Service
public class TotalCountWithInFixTimeFrequencyController extends AbstractFrequencyControlService<FrequencyControlDTO> {


    /**
     * 是否达到限流阈值 子类实现 每个子类都可以自定义自己的限流逻辑判断
     * @param frequencyControlMap 定义的注解频控 Map中的Key-对应redis的单个频控的Key Map中的Value-对应redis的单个频控的Key限制的Value
     * @return true-方法被限流 false-方法没有被限流
     */
    @Override
    protected boolean reachRateLimit(Map<String, FrequencyControlDTO> frequencyControlMap) {
        //批量获取redis统计的值
        List<String> frequencyKeys = new ArrayList<>(frequencyControlMap.keySet());
        List<Integer> countList = RedisUtils2.mget(frequencyKeys, Integer.class);
        for (int i = 0; i < frequencyKeys.size(); i++) {
            String key = frequencyKeys.get(i);
            Integer count = countList.get(i);
            int frequencyControlCount = frequencyControlMap.get(key).getCount();
            if (Objects.nonNull(count) && count >= frequencyControlCount) {
                //频率超过了
                log.warn("frequencyControl limit key:{},count:{}", key, count);
                return true;
            }
        }
        return false;
    }

    /**
     * 增加限流统计次数 子类实现 每个子类都可以自定义自己的限流统计信息增加的逻辑
     * @param frequencyControlMap 定义的注解频控 Map中的Key-对应redis的单个频控的Key Map中的Value-对应redis的单个频控的Key限制的Value
     */
    @Override
    protected void addFrequencyControlStatisticsCount(Map<String, FrequencyControlDTO> frequencyControlMap) {
        frequencyControlMap.forEach((k, v) -> RedisUtils2.inc(k, v.getTime(), v.getUnit()));
    }

    @Override
    protected String getStrategyName() {
        return TOTAL_COUNT_WITH_IN_FIX_TIME_FREQUENCY_CONTROLLER;
    }
}
