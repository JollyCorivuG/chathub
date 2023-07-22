package com.jhc.chathub.controller;

import com.jhc.chathub.common.enums.ErrorStatus;
import com.jhc.chathub.common.resp.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "用户相关接口")
public class UserController {
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Response<String> login() {
        return Response.success("登录成功");
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Response<Void> register() {
        return Response.fail(ErrorStatus.SERVER_ERROR);
    }
}
