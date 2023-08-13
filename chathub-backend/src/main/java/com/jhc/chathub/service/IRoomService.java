package com.jhc.chathub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.chathub.pojo.entity.Room;

import java.util.List;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/13
 */
public interface IRoomService extends IService<Room> {
    List<Long> listUserIdsByRoomId(Long roomId);
}
