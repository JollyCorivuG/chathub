package bupt.edu.jhc.chathub.server.user.controller;

import bupt.edu.jhc.chathub.common.domain.constants.RedisConstants;
import bupt.edu.jhc.chathub.common.domain.vo.resp.Response;
import bupt.edu.jhc.chathub.common.utils.context.RequestHolder;
import bupt.edu.jhc.chathub.server.user.domain.dto.LoginFormDTO;
import bupt.edu.jhc.chathub.server.user.domain.dto.PhoneLoginFormDTO;
import bupt.edu.jhc.chathub.server.user.domain.dto.RegisterFormDTO;
import bupt.edu.jhc.chathub.server.user.domain.vo.UserVO;
import bupt.edu.jhc.chathub.server.user.service.IUserService;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 用户接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@RestController
@RequestMapping("/api/users")
@Api(tags = "用户相关接口")
@Slf4j
public class UserController {
    @Resource
    private IUserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/login")
    @ApiOperation("账号密码登录")
    public Response<String> login(@RequestBody LoginFormDTO loginForm) {
        return userService.login(loginForm);
    }

    @GetMapping("/phone_code/{phone}")
    @ApiOperation("获取手机验证码")
    public Response<Void> phoneCode(@PathVariable("phone") String phone) {
        return userService.phoneCode(phone);
    }

    @PostMapping("/phone_login")
    @ApiOperation("手机验证码登录")
    public Response<String> phoneLogin(@RequestBody PhoneLoginFormDTO phoneLoginForm) {
        return userService.phoneLogin(phoneLoginForm);
    }

    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Response<String> register(@RequestBody RegisterFormDTO registerForm) {
        return userService.register(registerForm);
    }

    @GetMapping("/info/me")
    @ApiOperation("获取当前登录用户信息")
    public Response<UserVO> getSelfInfo() {
        Long selfId = RequestHolder.get().getUid();
        return userService.getUserInfo(selfId, selfId);
    }

    @PutMapping("/info/me")
    @ApiOperation("更新当前登录用户信息")
    public Response<UserVO> updateSelfInfo(@RequestBody UserVO userVO) {
        Long selfId = RequestHolder.get().getUid();
        return userService.updateUserInfo(selfId, userVO);
    }


    @GetMapping("/info/{userId}")
    @ApiOperation("根据id获取用户信息")
    public Response<UserVO> getUserInfoById(@PathVariable("userId") Long userId) {
        Long selfId = RequestHolder.get().getUid();
        return userService.getUserInfo(selfId, userId);
    }

    @GetMapping("/logout")
    @ApiOperation("退出登录")
    public Response<Void> logout() {
        Long selfId = RequestHolder.get().getUid();
        String token = stringRedisTemplate.opsForValue().get(RedisConstants.ID_TO_TOKEN + selfId);
        if (token != null) {
            stringRedisTemplate.delete(RedisConstants.USER_TOKEN_KEY + token);
            stringRedisTemplate.opsForSet().remove(RedisConstants.ONLINE_USER_KEY, token);
        }
        stringRedisTemplate.delete(RedisConstants.ID_TO_TOKEN + selfId);
        return Response.success(null);
    }

    @GetMapping("/query/{keyword}")
    @ApiOperation("根据关键字查询用户")
    public Response<List<UserVO>> queryByKeyword(@PathVariable("keyword") String keyword, @RequestParam(value = "page", defaultValue = "1") Integer page) {
        // 先从缓存中查询
        String cacheData = stringRedisTemplate.opsForValue().get(RedisConstants.CACHE_QUERY_USER_KET + keyword + ":" + page.toString());
        if (cacheData != null) {
            log.info("从缓存中查询用户: {}", cacheData);
            return Response.success(JSONUtil.toList(JSONUtil.parseArray(cacheData), UserVO.class));
        }

        // 如果缓存中没有，再从数据库中查询
        return userService.queryByKeyword(keyword, page);
    }
}
