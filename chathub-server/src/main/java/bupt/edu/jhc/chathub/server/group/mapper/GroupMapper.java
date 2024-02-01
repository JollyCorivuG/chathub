package bupt.edu.jhc.chathub.server.group.mapper;

import bupt.edu.jhc.chathub.server.group.domain.entity.Group;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: 群组 mapper
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Mapper
public interface GroupMapper extends BaseMapper<Group> {
}
