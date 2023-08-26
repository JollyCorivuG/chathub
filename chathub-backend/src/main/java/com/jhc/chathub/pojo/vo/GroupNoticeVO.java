package com.jhc.chathub.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupNoticeVO {
    private Long id;
    private GroupVO showGroupInfo;
    private String description;
    private Integer statusInfo;
    private LocalDateTime createTime;
}
