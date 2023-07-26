package com.jhc.chathub.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("tb_friend_notice")
public class FriendNotice {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long connectUserId;
    private Long otherUserId;
    private String description;
    private Integer noticeType;
    private Integer statusInfo;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
