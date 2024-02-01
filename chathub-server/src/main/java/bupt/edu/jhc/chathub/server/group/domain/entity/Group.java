package bupt.edu.jhc.chathub.server.group.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Description: 群组实体类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_group")
public class Group {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 群号
     */
    @TableField("number")
    private String number;

    /**
     * 群组名称
     */
    @TableField("name")
    private String name;

    /**
     * 群组头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 群组人数
     */
    @TableField("people_num")
    private Integer peopleNum;

    /**
     * 群组最大人数
     */
    @TableField("max_people_num")
    private Integer maxPeopleNum;

    /**
     * 群主id
     */
    @TableField("owner_id")
    private Long ownerId;

    /**
     * 对应的房间id
     */
    @TableField("room_id")
    private Long roomId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}

