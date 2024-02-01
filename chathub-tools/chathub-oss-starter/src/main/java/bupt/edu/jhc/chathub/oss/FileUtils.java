package bupt.edu.jhc.chathub.oss;

import cn.hutool.core.lang.UUID;

/**
 * @Description: 文件工具类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/18
 */
public class FileUtils {
    /**
     * 生成随机文件名
     * @return String
     */
    public static String generateRandomFileName() {
        return UUID.randomUUID().toString(true);
    }
}
