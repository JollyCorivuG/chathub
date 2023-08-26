package com.jhc.chathub.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Description: 群组
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/23
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
    private String number;

    /**
     * 群组名称
     */
    private String name;

    /**
     * 群组头像
     */
    private String avatar;

    /**
     * 群组人数
     */
    private Integer peopleNum;

    /**
     * 群组最大人数
     */
    private Integer maxPeopleNum;

    /**
     * 群主id
     */
    private Long ownerId;

    /**
     * 对应的房间id
     */
    private Long roomId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
