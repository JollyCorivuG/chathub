package bupt.edu.jhc.chathub.common.utils;

import bupt.edu.jhc.chathub.common.domain.enums.ErrorStatus;
import bupt.edu.jhc.chathub.common.utils.exception.ThrowUtils;
import cn.hutool.core.util.RandomUtil;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @Description: 密码加密工具类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/18
 */
public class PasswordEncoder {
    public static String encode(String password) {
        String salt = RandomUtil.randomString(20);
        return encode(password,salt);
    }
    private static String encode(String password, String salt) {
        return salt + "@" + DigestUtils.md5DigestAsHex((password + salt).getBytes(StandardCharsets.UTF_8));
    }
    public static Boolean matches(String encodedPassword, String rawPassword) {
        if (Objects.isNull(encodedPassword) || Objects.isNull(rawPassword)) {
            return false;
        }
        ThrowUtils.throwIf(!encodedPassword.contains("@"), ErrorStatus.PARAMS_ERROR, "密码格式不正确!");
        String[] arr = encodedPassword.split("@");
        String salt = arr[0];
        return encodedPassword.equals(encode(rawPassword, salt));
    }
}
