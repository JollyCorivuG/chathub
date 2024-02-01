package bupt.edu.jhc.chathub.oss.minio;

import bupt.edu.jhc.chathub.oss.OssProperties;
import io.minio.MinioClient;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: minio 配置
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/22
 */
@Configuration
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnExpression("${oss.enabled}") // 当配置文件中 oss.enabled=true 时，才会加载该配置类
@ConditionalOnProperty(value = "oss.type", havingValue = "minio") // 当配置文件中 oss.type=minio 时，才会加载该配置类
public class MinioConfig {
    @Bean
    @SneakyThrows
    @ConditionalOnMissingBean(MinioClient.class)
    public MinioClient minioClient(OssProperties ossProperties) {
        return MinioClient.builder()
                .endpoint(ossProperties.getEndpoint())
                .credentials(ossProperties.getAccessKey(), ossProperties.getSecretKey())
                .build();
    }

    @Bean
    @ConditionalOnBean({MinioClient.class})
    @ConditionalOnMissingBean(MinioTemplate.class)
    public MinioTemplate minioTemplate(MinioClient minioClient, OssProperties ossProperties) {
        return new MinioTemplate(minioClient, ossProperties);
    }
}
