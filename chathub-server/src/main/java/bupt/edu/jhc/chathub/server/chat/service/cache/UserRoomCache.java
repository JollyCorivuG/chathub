package bupt.edu.jhc.chathub.server.chat.service.cache;

import bupt.edu.jhc.chathub.common.domain.constants.RedisConstants;
import bupt.edu.jhc.chathub.common.service.cache.AbstractRedisStringCache;
import bupt.edu.jhc.chathub.server.chat.domain.entity.UserRoom;
import bupt.edu.jhc.chathub.server.chat.mapper.UserRoomMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import kotlin.Pair;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description: 用户房间对应信息缓存
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/2/11
 */
@Component
public class UserRoomCache extends AbstractRedisStringCache<Pair<Long, Long>, UserRoom> {
    @Resource
    private UserRoomMapper userRoomMapper;

    @Override
    protected String getKey(Pair<Long, Long> req) {
        return RedisConstants.USER_ROOM_INFO + req.getFirst() + ":" + req.getSecond();
    }

    @Override
    protected Long getExpireSeconds() {
        return 5 * 60L;
    }

    @Override
    protected Map<Pair<Long, Long>, UserRoom> load(List<Pair<Long, Long>> req) {
        List<UserRoom> userRooms = req.stream().map(p -> {
            QueryWrapper<UserRoom> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", p.getFirst());
            queryWrapper.eq("room_id", p.getSecond());
            return userRoomMapper.selectList(queryWrapper).isEmpty() ? null : userRoomMapper.selectList(queryWrapper).get(0);
        }).collect(Collectors.toList());
        return userRooms.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(userRoom -> new Pair<>(userRoom.getUserId(), userRoom.getRoomId()), userRoom -> userRoom));
    }
}
