package bupt.edu.jhc.chathub.server.user.service.impl;

import bupt.edu.jhc.chathub.common.domain.constants.RedisConstants;
import bupt.edu.jhc.chathub.common.domain.constants.SystemConstants;
import bupt.edu.jhc.chathub.common.domain.enums.ErrorStatus;
import bupt.edu.jhc.chathub.common.domain.vo.resp.Response;
import bupt.edu.jhc.chathub.common.utils.PasswordEncoder;
import bupt.edu.jhc.chathub.common.utils.context.RequestHolder;
import bupt.edu.jhc.chathub.common.utils.context.RequestInfo;
import bupt.edu.jhc.chathub.common.utils.exception.ThrowUtils;
import bupt.edu.jhc.chathub.common.utils.regex.RegexUtils;
import bupt.edu.jhc.chathub.server.friend.domain.entity.FriendRelation;
import bupt.edu.jhc.chathub.server.friend.mapper.FriendRelationMapper;
import bupt.edu.jhc.chathub.server.group.domain.entity.Group;
import bupt.edu.jhc.chathub.server.group.domain.entity.GroupRelation;
import bupt.edu.jhc.chathub.server.group.mapper.GroupMapper;
import bupt.edu.jhc.chathub.server.group.mapper.GroupRelationMapper;
import bupt.edu.jhc.chathub.server.user.domain.dto.LoginFormDTO;
import bupt.edu.jhc.chathub.server.user.domain.dto.PhoneLoginFormDTO;
import bupt.edu.jhc.chathub.server.user.domain.dto.RegisterFormDTO;
import bupt.edu.jhc.chathub.server.user.domain.dto.UserDTO;
import bupt.edu.jhc.chathub.server.user.domain.entity.User;
import bupt.edu.jhc.chathub.server.user.domain.vo.UserVO;
import bupt.edu.jhc.chathub.server.user.mapper.UserMapper;
import bupt.edu.jhc.chathub.server.user.service.IUserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description: 用户服务实现类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private FriendRelationMapper friendRelationMapper;

    @Resource
    private Environment environment;

    @Resource
    private GroupMapper groupMapper;

    @Resource
    private GroupRelationMapper groupRelationMapper;

    private Response<String> releaseToken(User user) {
        // 1.生成token
        String token = UUID.randomUUID().toString(true);
        String userTokenKey = RedisConstants.USER_TOKEN_KEY + token;

        // 2.将user转换为userDTO存储到redis中, 并设置过期时间
        RequestInfo info = RequestInfo.builder().uid(user.getId()).build();
        Map<String, Object> userMap = BeanUtil.beanToMap(info, false, true);
        userMap.forEach((key, value) -> userMap.put(key, String.valueOf(value)));
        stringRedisTemplate.opsForHash().putAll(userTokenKey, userMap);
        stringRedisTemplate.expire(userTokenKey, RedisConstants.USER_TOKEN_KEY_TTL, TimeUnit.MINUTES);

        // 3.记录在线用户
        String originToken = stringRedisTemplate.opsForValue().get(RedisConstants.ID_TO_TOKEN + user.getId());
        if (originToken != null) {
            stringRedisTemplate.delete(RedisConstants.USER_TOKEN_KEY + originToken);
            stringRedisTemplate.opsForSet().remove(RedisConstants.ONLINE_USER_KEY, originToken);
        }
        stringRedisTemplate.opsForValue().set(RedisConstants.ID_TO_TOKEN + user.getId(), token);
        stringRedisTemplate.opsForSet().add(RedisConstants.ONLINE_USER_KEY, token);

        // 4.返回token
        return Response.success(token);
    }

    @Override
    public Response<String> login(LoginFormDTO loginForm) {
        // 1.根据账号查询用户
        List<User> users = query().eq("account", loginForm.getAccount()).list();
        ThrowUtils.throwIf(users.isEmpty(), ErrorStatus.PARAMS_ERROR, "账号不存在, 请先注册!");

        // 2.校验密码
        User user = users.get(0);
        ThrowUtils.throwIf(!PasswordEncoder.matches(user.getPassword(), loginForm.getPassword()), ErrorStatus.PARAMS_ERROR, "密码错误!");

        // 3.颁发token并返回数据
        return releaseToken(user);
    }

    @Override
    public Response<Void> phoneCode(String phone) {
        // 1.生成验证码
        String code = RandomUtil.randomNumbers(6);
        log.info("发送验证码成功: {}", code);

        // 2.将验证码保存到redis中
        stringRedisTemplate.opsForValue().set(RedisConstants.PHONE_CODE_KEY + phone, code, RedisConstants.PHONE_CODE_KEY_TTL, TimeUnit.MINUTES);

        // 3.返回成功
        return Response.success(null);
    }

    @Override
    public Response<String> phoneLogin(PhoneLoginFormDTO phoneLoginForm) {
        // 1.根据手机号查询用户
        List<User> users = query().eq("phone", phoneLoginForm.getPhone()).list();
        ThrowUtils.throwIf(users.isEmpty(), ErrorStatus.PARAMS_ERROR, "该手机号未注册账号, 请先注册!");

        // 2.校验验证码
        User user = users.get(0);
        String rightCode = stringRedisTemplate.opsForValue().get(RedisConstants.PHONE_CODE_KEY + phoneLoginForm.getPhone());
        ThrowUtils.throwIf(rightCode == null, ErrorStatus.OPERATION_ERROR, "验证码已过期, 请重新获取验证码!");
        ThrowUtils.throwIf(!rightCode.equals(phoneLoginForm.getCode()), ErrorStatus.PARAMS_ERROR, "验证码错误!");

        // 3.颁发token并返回数据
        return releaseToken(user);
    }

    @Override
    public Response<String> register(RegisterFormDTO registerForm) {
        // 1.先判断手机号和账号是否已经被注册
        List<User> users = query().eq("phone", registerForm.getPhone()).or().eq("account", registerForm.getAccount()).list();
        ThrowUtils.throwIf(!users.isEmpty(), ErrorStatus.PARAMS_ERROR, "该手机号或账号已被注册!");

        // 2.生成user实例并插入数据库
        User user = new User();
        String encodePassword = PasswordEncoder.encode(registerForm.getPassword());
        String nickName = SystemConstants.DEFAULT_NICK_NAME_PREFIX + RandomUtil.randomString(6);
        user.setAccount(registerForm.getAccount()).setPassword(encodePassword).setPhone(registerForm.getPhone()).setNickName(nickName);
        String defaultAvatarUrl = Objects.equals(environment.getProperty("spring.profiles.active"), "dev") ?
                SystemConstants.DEV_DEFAULT_USER_AVATAR_URL : SystemConstants.PROD_DEFAULT_USER_AVATAR_URL;
        user.setAvatarUrl(defaultAvatarUrl);
        save(user);

        // 3.颁发token并返回数据
        return releaseToken(user);
    }

    @Override
    public UserVO convertUserToUserVO(Long selfId, User user) {
        // 1.判断用户是否在线以及是否是自己好友
        Long userId = user.getId();
        String token = stringRedisTemplate.opsForValue().get(RedisConstants.ID_TO_TOKEN + userId);
        Boolean isOnline = !StrUtil.isBlank(token) &&
                Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(RedisConstants.ONLINE_USER_KEY, token));
        boolean isFriend = selfId.equals(userId) ||
                Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(RedisConstants.USER_FRIEND_KEY + selfId, userId.toString()));

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
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(RedisConstants.USER_TOKEN_KEY + token);
        if (userMap.isEmpty()) {
            return null;
        }
        return BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);
    }

    @Override
    public Response<UserVO> getUserInfo(Long selfId, Long userId) {
        // 1.查询用户信息
        User user = getById(userId);
        ThrowUtils.throwIf(user == null, ErrorStatus.NOT_FOUND_ERROR);

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
        Page<User> userPage = new Page<>(currentPage, SystemConstants.DEFAULT_PAGE_SIZE);
        userPage.setSearchCount(false); // 关闭查询总记录数

        // 4.查询用户
        Page<User> userRecords = page(userPage, queryWrapper);
        List<User> users = userRecords.getRecords();
        Long selfId = RequestHolder.get().getUid();
        List<UserVO> userVOs = users.stream().map(user -> {
            if (!Objects.equals(user.getId(), selfId)) {
                return convertUserToUserVO(selfId, user);
            }
            return null;
        }).collect(Collectors.toList());
        userVOs.removeIf(Objects::isNull);

        // 4.插入到redis缓存中
        String key = RedisConstants.CACHE_QUERY_USER_KET + vagueNickName + ":" + currentPage;
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(userVOs), RedisConstants.CACHE_QUERY_USER_KEY_TTL, TimeUnit.MINUTES);

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
        Set<String> friendsId = stringRedisTemplate.opsForSet().members(RedisConstants.USER_FRIEND_KEY + userId.toString());
        if (friendsId == null || friendsId.isEmpty()) {
            return new ArrayList<>();
        }
        return friendsId.stream().map(Long::parseLong).collect(Collectors.toList());
    }

    @Override
    public List<Long> queryGroupIds(Long userId) {
        // 1.先查询自己是群主的群组
        QueryWrapper<Group> wrapper1 = new QueryWrapper<Group>().eq("owner_id", userId);
        List<Long> groupIds = groupMapper.selectList(wrapper1).stream().map(Group::getId).collect(Collectors.toList());

        // 2.再查询自己加入的群组
        QueryWrapper<GroupRelation> wrapper2 = new QueryWrapper<GroupRelation>().eq("user_id", userId);
        List<Long> groupIds2 = groupRelationMapper.selectList(wrapper2).stream().map(GroupRelation::getGroupId).collect(Collectors.toList());

        // 3.一起返回
        return Stream.of(groupIds, groupIds2).flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public Response<UserVO> updateUserInfo(Long selfId, UserVO userVO) {
        // 1.根据id查询用户
        User user = getById(selfId);
        ThrowUtils.throwIf(user == null, ErrorStatus.NOT_FOUND_ERROR);

        // 2.更新用户信息
        user.setNickName(userVO.getNickName()).setAvatarUrl(userVO.getAvatarUrl());
        updateById(user);

        // 3.返回成功
        return Response.success(convertUserToUserVO(selfId, user));
    }
}
