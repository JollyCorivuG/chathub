package com.jhc.chathub.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName(value = "tb_room")
public class Room {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Integer roomType;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
