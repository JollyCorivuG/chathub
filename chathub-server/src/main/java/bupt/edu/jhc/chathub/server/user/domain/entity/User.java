package bupt.edu.jhc.chathub.server.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Description: 用户实体对象
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@Accessors(chain = true)
@TableName("tb_user")
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("account")
    private String account;
    @TableField("password")
    private String password;
    @TableField("phone")
    private String phone;
    @TableField("nick_name")
    private String nickName;
    @TableField("avatar_url")
    private String avatarUrl;
    @TableField("level")
    private Integer level;
    @TableField("friend_count")
    private Integer friendCount;
    @TableField("group_count")
    private Integer groupCount;
    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("update_time")
    private LocalDateTime updateTime;
}
