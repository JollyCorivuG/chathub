package com.jhc.chathub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.pojo.dto.user.LoginFormDTO;
import com.jhc.chathub.pojo.dto.user.PhoneLoginFormDTO;
import com.jhc.chathub.pojo.dto.user.RegisterFormDTO;
import com.jhc.chathub.pojo.dto.user.UserDTO;
import com.jhc.chathub.pojo.entity.User;
import com.jhc.chathub.pojo.vo.UserVO;

import java.util.List;

public interface IUserService extends IService<User> {
    Response<String> login(LoginFormDTO loginForm);

    Response<String> phoneLogin(PhoneLoginFormDTO phoneLoginForm);

    Response<String> register(RegisterFormDTO registerForm);

    Response<Void> phoneCode(String phone);

    Response<UserVO> getUserInfo(Long selfId, Long userId);

    Response<List<UserVO>> queryByKeyword(String keyword, Integer page);

    UserVO convertUserToUserVO(Long selfId, User user);

    UserDTO getUserByToken(String token);

    List<Long> queryFriendIds(Long userId);

    List<Long> queryGroupIds(Long userId);
}
