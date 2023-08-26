package com.jhc.chathub.pojo.dto.group;

import lombok.Data;

/**
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2023/8/23
 */
@Data
public class CreateGroupDTO {
    private String name;
    private String avatar;
    private Integer maxPeopleNum;
}
