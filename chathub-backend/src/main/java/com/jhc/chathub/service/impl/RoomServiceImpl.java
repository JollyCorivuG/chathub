package com.jhc.chathub.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.chathub.mapper.RoomMapper;
import com.jhc.chathub.pojo.entity.FriendRelation;
import com.jhc.chathub.pojo.entity.Room;
import com.jhc.chathub.service.IFriendService;
import com.jhc.chathub.service.IRoomService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/13
 */
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements IRoomService {

    @Resource
    private IFriendService friendService;

    @Override
    public List<Long> listUserIdsByRoomId(Long roomId) {
        List<Long> results = new ArrayList<>();

        // 1.先查询好友关系中的用户
        List<FriendRelation> friendRelationList = friendService.query().eq("room_id", roomId).list();
        friendRelationList.forEach(friendRelation -> {
            results.add(friendRelation.getUserId1());
            results.add(friendRelation.getUserId2());
        });

        // TODO 2.再查询群组中的用户

        // 3.返回结果
        return results;
    }
}
