package com.jhc.chathub.controller;

import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.pojo.dto.LoginFormDTO;
import com.jhc.chathub.pojo.dto.PhoneLoginFormDTO;
import com.jhc.chathub.pojo.dto.RegisterFormDTO;
import com.jhc.chathub.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "用户相关接口")
public class UserController {
    @Resource
    private IUserService userService;

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
}
