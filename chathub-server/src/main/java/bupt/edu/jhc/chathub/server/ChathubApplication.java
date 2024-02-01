package bupt.edu.jhc.chathub.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @Description: 主启动类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/16
 */
@SpringBootApplication(scanBasePackages = "bupt.edu.jhc.chathub")
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ServletComponentScan
@MapperScan({"bupt.edu.jhc.chathub.server.**.mapper"})
public class ChathubApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChathubApplication.class, args);
    }
}
