package com.jhc.chathub;

import com.jhc.chathub.common.constants.SystemConstant;
import com.jhc.chathub.mapper.UserMapper;
import com.jhc.chathub.pojo.dto.message.ImgMsgDTO;
import com.jhc.chathub.pojo.dto.message.MessageExtra;
import com.jhc.chathub.pojo.entity.Message;
import com.jhc.chathub.pojo.entity.User;
import com.jhc.chathub.service.IMessageService;
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

	@Resource
	private IMessageService messageService;
	@Test
	void testInsertMessage() {
		Message message = new Message();
		message.setFromUserId(1L);
		message.setRoomId(1L);
		ImgMsgDTO imgMsgDTO = new ImgMsgDTO();
		imgMsgDTO.setUrl("http://www.baidu.com");
		imgMsgDTO.setWidth(100);
		imgMsgDTO.setHeight(100);
		imgMsgDTO.setSize(100L);

		message.setExtra(new MessageExtra().setImgMsg(imgMsgDTO));
		message.setMsgType(0);
		messageService.save(message);
	}

	@Test
	void testQueryMessage() {
		Message message = messageService.getById(2);
		System.out.println(message.getExtra().getImgMsg().getUrl());
		System.out.println(message);
	}
}
