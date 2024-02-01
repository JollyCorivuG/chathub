package bupt.edu.jhc.chathub.frequency_control.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * @Description: 频控策略定义
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/2/1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FrequencyControlDTO {
    private String key;
    private Integer time;
    private TimeUnit unit;
    private Integer count;
}
