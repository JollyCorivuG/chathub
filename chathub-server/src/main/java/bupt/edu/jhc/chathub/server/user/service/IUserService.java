package bupt.edu.jhc.chathub.server.user.service;

import bupt.edu.jhc.chathub.common.domain.vo.resp.Response;
import bupt.edu.jhc.chathub.server.user.domain.dto.LoginFormDTO;
import bupt.edu.jhc.chathub.server.user.domain.dto.PhoneLoginFormDTO;
import bupt.edu.jhc.chathub.server.user.domain.dto.RegisterFormDTO;
import bupt.edu.jhc.chathub.server.user.domain.dto.UserDTO;
import bupt.edu.jhc.chathub.server.user.domain.entity.User;
import bupt.edu.jhc.chathub.server.user.domain.vo.UserVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 用户服务接口类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
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

    Response<UserVO> updateUserInfo(Long selfId, UserVO userVO);
}
