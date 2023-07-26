package com.jhc.chathub.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("tb_friend_relation")
public class FriendRelation {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long userId1;
    private Long userId2;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
