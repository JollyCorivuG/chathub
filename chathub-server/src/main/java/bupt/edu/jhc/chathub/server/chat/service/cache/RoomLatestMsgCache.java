package bupt.edu.jhc.chathub.server.chat.service.cache;

import bupt.edu.jhc.chathub.common.domain.constants.RedisConstants;
import bupt.edu.jhc.chathub.common.service.cache.AbstractRedisStringCache;
import bupt.edu.jhc.chathub.server.chat.domain.entity.Room;
import bupt.edu.jhc.chathub.server.chat.mapper.RoomMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 房间最新信息缓存
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/2/11
 */
@Component
public class RoomLatestMsgCache extends AbstractRedisStringCache<Long, Long>  {
    @Resource
    private RoomMapper roomMapper;

    @Override
    protected String getKey(Long roomId) {
        return RedisConstants.ROOM_LATEST_MESSAGE + roomId;
    }

    @Override
    protected Long getExpireSeconds() {
        return 5 * 60L;
    }

    @Override
    protected Map<Long, Long> load(List<Long> roomIds) {
        List<Room> rooms = roomMapper.selectBatchIds(roomIds);
        return rooms.stream()
                .collect(Collectors.toMap(Room::getId, Room::getLatestMsgId));
    }
}
