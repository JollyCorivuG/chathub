package com.jhc.chathub.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.chathub.common.constants.RedisConstant;
import com.jhc.chathub.common.constants.SystemConstant;
import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.mapper.UserMapper;
import com.jhc.chathub.pojo.dto.LoginFormDTO;
import com.jhc.chathub.pojo.dto.PhoneLoginFormDTO;
import com.jhc.chathub.pojo.dto.RegisterFormDTO;
import com.jhc.chathub.pojo.dto.UserDTO;
import com.jhc.chathub.pojo.entity.User;
import com.jhc.chathub.pojo.vo.UserVO;
import com.jhc.chathub.service.IUserService;
import com.jhc.chathub.utils.PasswordEncoder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private Response<String> releaseToken(User user) {
        // 1.生成token
        String token = UUID.randomUUID().toString(true);
        String userTokenKey = RedisConstant.USER_TOKEN_KEY + token;

        // 2.将user转换为userDTO存储到redis中, 并设置过期时间
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, false, true);
        userMap.forEach((key, value) -> userMap.put(key, String.valueOf(value)));
        stringRedisTemplate.opsForHash().putAll(userTokenKey, userMap);
        stringRedisTemplate.expire(userTokenKey, RedisConstant.USER_TOKEN_KEY_TTL, TimeUnit.MINUTES);

        // 3.记录在线用户
        stringRedisTemplate.opsForValue().set(RedisConstant.ID_TO_TOKEN + user.getId(), token);
        stringRedisTemplate.opsForSet().add(RedisConstant.ONLINE_USER_KEY, token);

        // 4.返回token
        return Response.success(token);
    }

    @Override
    public Response<String> login(LoginFormDTO loginForm) {
        // 1.根据账号查询用户
        List<User> users = query().eq("account", loginForm.getAccount()).list();
        if (users.isEmpty()) {
            return Response.fail("账号不存在, 请先注册!");
        }

        // 2.校验密码
        User user = users.get(0);
        if (!PasswordEncoder.matches(user.getPassword(), loginForm.getPassword())) {
            return Response.fail("密码错误!");
        }

        // 3.颁发token并返回数据
        return releaseToken(user);
    }

    @Override
    public Response<Void> phoneCode(String phone) {
        // 1.生成验证码
        String code = RandomUtil.randomNumbers(6);
        log.info("发送验证码成功: {}", code);

        // 2.将验证码保存到redis中
        stringRedisTemplate.opsForValue().set(RedisConstant.PHONE_CODE_KEY + phone, code, RedisConstant.PHONE_CODE_KEY_TTL, TimeUnit.MINUTES);

        // 3.返回成功
        return Response.success(null);
    }

    @Override
    public Response<String> phoneLogin(PhoneLoginFormDTO phoneLoginForm) {
        // 1.根据手机号查询用户
        List<User> users = query().eq("phone", phoneLoginForm.getPhone()).list();
        if (users.isEmpty()) {
            return Response.fail("该手机号未注册账号, 请先注册!");
        }

        // 2.校验验证码
        User user = users.get(0);
        String rightCode = stringRedisTemplate.opsForValue().get(RedisConstant.PHONE_CODE_KEY + phoneLoginForm.getPhone());
        if (rightCode == null) {
            return Response.fail("验证码已过期, 请重新获取验证码!");
        }
        if (!rightCode.equals(phoneLoginForm.getCode())) {
            return Response.fail("验证码错误!");
        }

        // 3.颁发token并返回数据
        return releaseToken(user);
    }

    @Override
    public Response<String> register(RegisterFormDTO registerForm) {
        // 1.先判断手机号和账号是否已经被注册
        List<User> users = query().eq("phone", registerForm.getPhone()).or().eq("account", registerForm.getAccount()).list();
        if (!users.isEmpty()) {
            return Response.fail("该手机号或账号已被注册!");
        }

        // 2.生成user实例并插入数据库
        User user = new User();
        String encodePassword = PasswordEncoder.encode(registerForm.getPassword());
        String nickName = SystemConstant.DEFAULT_NICK_NAME_PREFIX + RandomUtil.randomString(6);
        user.setAccount(registerForm.getAccount()).setPassword(encodePassword).setPhone(registerForm.getPhone()).setNickName(nickName).setAvatarUrl(SystemConstant.DEFAULT_USER_AVATAR_URL);
        save(user);

        // 3.颁发token并返回数据
        return releaseToken(user);
    }

    @Override
    public Response<UserVO> getUserInfo(Long selfId, Long userId) {
        // 1.查询用户信息
        User user = getById(userId);
        if (user == null) {
            return Response.fail("所要查询用户不存在!");
        }

        // 2.判断用户是否在线以及是否是自己好友
        String token = stringRedisTemplate.opsForValue().get(RedisConstant.ID_TO_TOKEN + userId);
        Boolean isOnline = !StrUtil.isBlank(token) &&
                Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(RedisConstant.ONLINE_USER_KEY, userId.toString()));
        Boolean isFriend = selfId.equals(userId) ||
                Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(RedisConstant.USER_FRIEND_KEY + selfId, userId.toString()));

        // 3.将用户信息转换为UserVO返回
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        userVO.setIsOnline(isOnline).setIsFriend(isFriend);

        // 4.返回数据
        return Response.success(userVO);
    }
}
