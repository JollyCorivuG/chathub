package com.jhc.chathub.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.chathub.common.constants.RedisConstant;
import com.jhc.chathub.common.constants.SystemConstant;
import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.mapper.FriendRelationMapper;
import com.jhc.chathub.mapper.UserMapper;
import com.jhc.chathub.pojo.dto.user.LoginFormDTO;
import com.jhc.chathub.pojo.dto.user.PhoneLoginFormDTO;
import com.jhc.chathub.pojo.dto.user.RegisterFormDTO;
import com.jhc.chathub.pojo.dto.user.UserDTO;
import com.jhc.chathub.pojo.entity.FriendRelation;
import com.jhc.chathub.pojo.entity.User;
import com.jhc.chathub.pojo.vo.UserVO;
import com.jhc.chathub.service.IUserService;
import com.jhc.chathub.utils.PasswordEncoder;
import com.jhc.chathub.utils.RegexUtils;
import com.jhc.chathub.utils.UserHolder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private FriendRelationMapper friendRelationMapper;

    @Resource
    private Environment environment;

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
        user.setAccount(registerForm.getAccount()).setPassword(encodePassword).setPhone(registerForm.getPhone()).setNickName(nickName);
        String defaultAvatarUrl = Objects.equals(environment.getProperty("spring.profiles.active"), "dev") ?
                SystemConstant.DEV_DEFAULT_USER_AVATAR_URL : SystemConstant.PROD_DEFAULT_USER_AVATAR_URL;
        user.setAvatarUrl(defaultAvatarUrl);
        save(user);

        // 3.颁发token并返回数据
        return releaseToken(user);
    }

    @Override
    public UserVO convertUserToUserVO(Long selfId, User user) {
        // 1.判断用户是否在线以及是否是自己好友
        Long userId = user.getId();
        String token = stringRedisTemplate.opsForValue().get(RedisConstant.ID_TO_TOKEN + userId);
        Boolean isOnline = !StrUtil.isBlank(token) &&
                Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(RedisConstant.ONLINE_USER_KEY, token));
        boolean isFriend = selfId.equals(userId) ||
                Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(RedisConstant.USER_FRIEND_KEY + selfId, userId.toString()));

        // 2.将用户信息转换为UserVO
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        if (isFriend && !selfId.equals(userId)) {
            QueryWrapper<FriendRelation> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id1", selfId).eq("user_id2", userId).or()
                    .eq("user_id1", userId).eq("user_id2", selfId);
            FriendRelation friendRelation = friendRelationMapper.selectOne(queryWrapper);
            userVO.setBecomeFriendTime(friendRelation.getCreateTime());
            userVO.setRoomId(friendRelation.getRoomId());
        }
        return userVO.setIsOnline(isOnline).setIsFriend(isFriend);
    }

    @Override
    public UserDTO getUserByToken(String token) {
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(RedisConstant.USER_TOKEN_KEY + token);
        if (userMap.isEmpty()) {
            return null;
        }
        return BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);
    }

    @Override
    public Response<UserVO> getUserInfo(Long selfId, Long userId) {
        // 1.查询用户信息
        User user = getById(userId);
        if (user == null) {
            return Response.fail("所要查询用户不存在!");
        }

        // 2.将user转换为userVO返回
        return Response.success(convertUserToUserVO(selfId, user));
    }

    private List<UserVO> queryWithCondition(String vagueNickName, boolean isPhone, boolean isAccount, Integer currentPage) {
        // 1.创建条件构造器
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        // 2.模糊昵称是一定的, 然后根据isPhone和isAccount判断可能是根据什么查询
        queryWrapper.like("nick_name", vagueNickName);
        if (isPhone) {
            queryWrapper.or().eq("phone", vagueNickName);
        }
        if (isAccount) {
            queryWrapper.or().eq("account", vagueNickName);
        }

        // 3.分页查询
        queryWrapper.orderByDesc("id");
        Page<User> userPage = new Page<>(currentPage, SystemConstant.DEFAULT_PAGE_SIZE);
        userPage.setSearchCount(false); // 关闭查询总记录数

        // 4.查询用户
        Page<User> userRecords = page(userPage, queryWrapper);
        List<User> users = userRecords.getRecords();
        Long selfId = UserHolder.getUser().getId();
        List<UserVO> userVOs = new ArrayList<>(users.stream().map(user -> {
            if (!Objects.equals(user.getId(), selfId)) {
                return convertUserToUserVO(selfId, user);
            }
            return null;
        }).toList());
        userVOs.removeIf(Objects::isNull);

        // 4.插入到redis缓存中
        String key = RedisConstant.CACHE_QUERY_USER_KET + vagueNickName + ":" + currentPage;
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(userVOs), RedisConstant.CACHE_QUERY_USER_KEY_TTL, TimeUnit.MINUTES);

        // 5.返回结果
        return userVOs;
    }

    @Override
    public Response<List<UserVO>> queryByKeyword(String keyword, Integer page) {
        // 1.先校验keyword, 判断可能是根据什么查询
        boolean isPhone = RegexUtils.isPhoneValid(keyword);
        boolean isAccount = RegexUtils.isAccountValid(keyword);

        // 2.返回结果
        return Response.success(queryWithCondition(keyword, isPhone, isAccount, page));
    }

    @Override
    public List<Long> queryFriendIds(Long userId) {
        Set<String> friendsId = stringRedisTemplate.opsForSet().members(RedisConstant.USER_FRIEND_KEY + userId.toString());
        if (friendsId == null || friendsId.isEmpty()) {
            return new ArrayList<>();
        }
        return friendsId.stream().map(Long::parseLong).collect(Collectors.toList());
    }

    @Override
    public List<Long> queryGroupIds(Long userId) {
        // TODO
        return Collections.emptyList();
    }
}
