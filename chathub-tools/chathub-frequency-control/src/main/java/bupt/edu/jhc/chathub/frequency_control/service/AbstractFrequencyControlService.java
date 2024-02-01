package bupt.edu.jhc.chathub.frequency_control.service;

import bupt.edu.jhc.chathub.common.domain.enums.ErrorStatus;
import bupt.edu.jhc.chathub.common.utils.exception.ThrowUtils;
import bupt.edu.jhc.chathub.frequency_control.domain.FrequencyControlDTO;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 频控抽象服务类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/2/1
 */
public abstract class AbstractFrequencyControlService<K extends FrequencyControlDTO> {
    @PostConstruct
    protected void registerMyselfToFactory() {
        FrequencyControlStrategyFactory.registerFrequencyController(getStrategyName(), this);
    }

    private <T> T executeWithFrequencyControlMap(Map<String, K> frequencyControlMap, SupplierThrowWithoutParam<T> supplier) throws Throwable {
        ThrowUtils.throwIf(reachRateLimit(frequencyControlMap), ErrorStatus.FORBIDDEN_ERROR, "操作过于频繁，请稍后再试!");

        try {
            return supplier.get();
        } finally {
            //不管成功还是失败，都增加次数
            addFrequencyControlStatisticsCount(frequencyControlMap);
        }
    }

    public <T> T executeWithFrequencyControlList(List<K> frequencyControlList, SupplierThrowWithoutParam<T> supplier) throws Throwable {
        boolean existsFrequencyControlHasNullKey = frequencyControlList.stream().anyMatch(frequencyControl -> ObjectUtils.isEmpty(frequencyControl.getKey()));
        ThrowUtils.throwIf(existsFrequencyControlHasNullKey, ErrorStatus.SYSTEM_ERROR, "限流策略的 Key 字段不允许出现空值");
        Map<String, FrequencyControlDTO> frequencyControlDTOMap = frequencyControlList.stream().collect(Collectors.groupingBy(FrequencyControlDTO::getKey, Collectors.collectingAndThen(Collectors.toList(), list -> list.get(0))));
        return executeWithFrequencyControlMap((Map<String, K>) frequencyControlDTOMap, supplier);
    }

    public <T> T executeWithFrequencyControl(K frequencyControl, SupplierThrowWithoutParam<T> supplier) throws Throwable {
        return executeWithFrequencyControlList(Collections.singletonList(frequencyControl), supplier);
    }


    @FunctionalInterface
    public interface SupplierThrowWithoutParam<T> {

        /**
         * Gets a result.
         *
         * @return a result
         */
        T get() throws Throwable;
    }

    @FunctionalInterface
    public interface Executor {

        /**
         * Gets a result.
         *
         * @return a result
         */
        void execute() throws Throwable;
    }

    /**
     * 是否达到限流阈值 子类实现 每个子类都可以自定义自己的限流逻辑判断
     *
     * @param frequencyControlMap 定义的注解频控 Map中的Key-对应redis的单个频控的Key Map中的Value-对应redis的单个频控的Key限制的Value
     * @return true-方法被限流 false-方法没有被限流
     */
    protected abstract boolean reachRateLimit(Map<String, K> frequencyControlMap);

    /**
     * 增加限流统计次数 子类实现 每个子类都可以自定义自己的限流统计信息增加的逻辑
     *
     * @param frequencyControlMap 定义的注解频控 Map中的Key-对应redis的单个频控的Key Map中的Value-对应redis的单个频控的Key限制的Value
     */
    protected abstract void addFrequencyControlStatisticsCount(Map<String, K> frequencyControlMap);

    /**
     * 获取策略名称
     *
     * @return 策略名称
     */
    protected abstract String getStrategyName();
}
