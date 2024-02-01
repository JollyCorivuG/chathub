package bupt.edu.jhc.chathub.server.chat.service;

import bupt.edu.jhc.chathub.server.chat.domain.entity.Room;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 房间服务接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
public interface IRoomService extends IService<Room> {
    List<Long> listUserIdsByRoomId(Long roomId);
}