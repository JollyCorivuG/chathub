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
 * @Description: 群组通知
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_group_notice")
public class GroupNotice {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long groupId;
    private String description;
    private Integer statusInfo;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
