package com.jhc.chathub;

import com.jhc.chathub.mapper.UserMapper;
import com.jhc.chathub.pojo.entity.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChathubApplicationTests {
	@Resource
	private UserMapper userMapper;
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

}
