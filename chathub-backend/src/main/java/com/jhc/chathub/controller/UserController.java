package com.jhc.chathub.controller;

import com.jhc.chathub.common.constants.RedisConstant;
import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.pojo.dto.LoginFormDTO;
import com.jhc.chathub.pojo.dto.PhoneLoginFormDTO;
import com.jhc.chathub.pojo.dto.RegisterFormDTO;
import com.jhc.chathub.pojo.vo.UserVO;
import com.jhc.chathub.service.IUserService;
import com.jhc.chathub.utils.UserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "用户相关接口")
public class UserController {
    @Resource
    private IUserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/login")
    @Operation(summary = "账号密码登录")
    public Response<String> login(@RequestBody LoginFormDTO loginForm) {
        return userService.login(loginForm);
    }

    @GetMapping("/phone_code/{phone}")
    @Operation(summary = "获取手机验证码")
    public Response<Void> phoneCode(@PathVariable("phone") String phone) {
        return userService.phoneCode(phone);
    }

    @PostMapping("/phone_login")
    @Operation(summary = "手机验证码登录")
    public Response<String> phoneLogin(@RequestBody PhoneLoginFormDTO phoneLoginForm) {
        return userService.phoneLogin(phoneLoginForm);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Response<String> register(@RequestBody RegisterFormDTO registerForm) {
        return userService.register(registerForm);
    }

    @GetMapping("/info/me")
    @Operation(summary = "获取当前登录用户信息")
    public Response<UserVO> getSelfInfo() {
        Long selfId = UserHolder.getUser().getId();
        return userService.getUserInfo(selfId, selfId);
    }

    @GetMapping("/info/{userId}")
    @Operation(summary = "根据id获取用户信息")
    public Response<UserVO> getUserInfoById(@PathVariable("userId") Long userId) {
        Long selfId = UserHolder.getUser().getId();
        return userService.getUserInfo(selfId, userId);
    }

    @GetMapping("/logout")
    @Operation(summary = "退出登录")
    public Response<Void> logout() {
        Long selfId = UserHolder.getUser().getId();
        String token = stringRedisTemplate.opsForValue().get(RedisConstant.ID_TO_TOKEN + selfId);
        if (token != null) {
            stringRedisTemplate.opsForSet().remove(RedisConstant.ONLINE_USER_KEY, token);
        }
        stringRedisTemplate.delete(RedisConstant.ID_TO_TOKEN + selfId);
        return Response.success(null);
    }
}
