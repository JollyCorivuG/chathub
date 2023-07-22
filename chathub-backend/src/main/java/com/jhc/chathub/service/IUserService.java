package com.jhc.chathub.service;

import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.pojo.dto.LoginFormDTO;
import com.jhc.chathub.pojo.dto.PhoneLoginFormDTO;
import com.jhc.chathub.pojo.dto.RegisterFormDTO;

public interface IUserService {
    Response<String> login(LoginFormDTO loginForm);

    Response<String> phoneLogin(PhoneLoginFormDTO phoneLoginForm);

    Response<String> register(RegisterFormDTO registerForm);

    Response<Void> phoneCode(String phone);
}
