package com.jhc.chathub;

import com.jhc.chathub.common.constants.SystemConstant;
import com.jhc.chathub.mapper.UserMapper;
import com.jhc.chathub.pojo.entity.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;

@SpringBootTest
class ChathubApplicationTests {
	@Resource
	private UserMapper userMapper;
	@Resource
	private StringRedisTemplate stringRedisTemplate;
	@Test
	void contextLoads() {
	}
	@Test
	void testInsertUser() {
		User user = new User();
		// 填充user所有属性
		user.setAccount("hug");
		user.setPhone("123456789");
		user.setNickName("虎哥");
		userMapper.insert(user);
	}

	@Test
	void testQueryUser() {
		User user = userMapper.selectById(6);
		System.out.println(user);
	}

	@Test
	void testRedis() {
		Set<String> cacheData = stringRedisTemplate.opsForSet().members("cacheData");
	}

	@Test
	void testInsertSomeUser() {
		// 生成100个用户
		for (int i = 0; i < 100; i++) {
			User user = new User();
			// 填充user所有属性
			user.setAccount("hug" + i);
			user.setPassword("123456" + i);
			user.setPhone("123456789" + i);
			user.setNickName("虎哥" + i);
			user.setAvatarUrl(SystemConstant.DEFAULT_USER_AVATAR_URL);
			userMapper.insert(user);
		}
	}

}
