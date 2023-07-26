package com.jhc.chathub.service;

import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.pojo.dto.LoginFormDTO;
import com.jhc.chathub.pojo.dto.PhoneLoginFormDTO;
import com.jhc.chathub.pojo.dto.RegisterFormDTO;
import com.jhc.chathub.pojo.vo.UserVO;

import java.util.List;

public interface IUserService {
    Response<String> login(LoginFormDTO loginForm);

    Response<String> phoneLogin(PhoneLoginFormDTO phoneLoginForm);

    Response<String> register(RegisterFormDTO registerForm);

    Response<Void> phoneCode(String phone);

    Response<UserVO> getUserInfo(Long selfId, Long userId);

    Response<List<UserVO>> queryByKeyword(String keyword, Integer page);
}
