package bupt.edu.jhc.chathub.server.user.mapper;

import bupt.edu.jhc.chathub.server.user.domain.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: 用户 mapper
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
