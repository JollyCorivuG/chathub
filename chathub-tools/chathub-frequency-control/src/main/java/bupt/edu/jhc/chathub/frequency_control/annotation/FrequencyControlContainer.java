package bupt.edu.jhc.chathub.frequency_control.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 频控注解容器
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/2/1
 */
@Retention(RetentionPolicy.RUNTIME) // 运行时生效
@Target(ElementType.METHOD) // 作用在方法上
public @interface FrequencyControlContainer {
    FrequencyControl[] value();
}
