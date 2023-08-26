package com.jhc.chathub.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupVO {
    private Long id;
    private String number;
    private String name;
    private String avatar;
    private Integer peopleNum;
    private UserVO owner;
    private List<UserVO> members;
    private Long roomId;
    private LocalDateTime createTime;
}
