package bupt.edu.jhc.chathub.frequency_control.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 频控注解
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/2/1
 */
@Repeatable(FrequencyControlContainer.class) // 可重复
@Retention(RetentionPolicy.RUNTIME) // 运行时生效
@Target(ElementType.METHOD) // 作用在方法上
public @interface FrequencyControl {
    /**
     * key 的前缀, 默认取方法全限定名, 除非在不同方法上对同一个资源做频控, 就自己指定
     * @return
     */
    String prefixKey() default "";

    /**
     * 频控对象, 默认 el 表达式指定具体的频控对象
     * 对于 UID 模式, 需要是 http 入口的对象, 保证 RequestHolder 里有值
     * @return
     */
    Target target() default Target.EL;

    /**
     * springEl 表达式, target = EL 必填
     * @return
     */
    String spEl() default "";

    /**
     * 频控时间范围, 默认单位秒
     * @return
     */
    int time();

    /**
     * 频控时间单位, 默认秒
     * @return 单位
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 单位时间内最大访问次数
     * @return 次数
     */
    int count();


    enum Target {
        UID, EL
    }
}
