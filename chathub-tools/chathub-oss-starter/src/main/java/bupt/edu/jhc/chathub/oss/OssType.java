package bupt.edu.jhc.chathub.oss;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: OSS 类型
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/22
 */
@Getter
@AllArgsConstructor
public enum OssType {
    /**
     * Minio 对象存储
     */
    MINIO("minio", 1),

    /**
     * 华为 OBS
     */
    OBS("obs", 2),

    /**
     * 腾讯 COS
     */
    COS("tencent", 3),

    /**
     * 阿里巴巴 SSO
     */
    ALIBABA("alibaba", 4),
    ;

    /**
     * 名称
     */
    final String name;
    /**
     * 类型
     */
    final int type;

}

