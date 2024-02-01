package bupt.edu.jhc.chathub.frequency_control.utils;

import bupt.edu.jhc.chathub.common.domain.enums.ErrorStatus;
import bupt.edu.jhc.chathub.common.utils.exception.ThrowUtils;
import bupt.edu.jhc.chathub.frequency_control.domain.FrequencyControlDTO;
import bupt.edu.jhc.chathub.frequency_control.service.AbstractFrequencyControlService;
import bupt.edu.jhc.chathub.frequency_control.service.FrequencyControlStrategyFactory;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @Description: 频控工具类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/2/1
 */
public class FrequencyControlUtils {
    public static <T, K extends FrequencyControlDTO> T executeWithFrequencyControlList(String strategyName, List<K> frequencyControlList, AbstractFrequencyControlService.SupplierThrowWithoutParam<T> supplier) throws Throwable {
        boolean existsFrequencyControlHasNullKey = frequencyControlList.stream().anyMatch(frequencyControl -> ObjectUtils.isEmpty(frequencyControl.getKey()));
        ThrowUtils.throwIf(existsFrequencyControlHasNullKey, ErrorStatus.SYSTEM_ERROR, "限流策略的 Key 字段不允许出现空值");
        AbstractFrequencyControlService<K> frequencyController = FrequencyControlStrategyFactory.getFrequencyControllerByName(strategyName);
        return frequencyController.executeWithFrequencyControlList(frequencyControlList, supplier);
    }
}
