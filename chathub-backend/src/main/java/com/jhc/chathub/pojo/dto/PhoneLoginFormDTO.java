package com.jhc.chathub.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PhoneLoginFormDTO {
    private String phone;
    private String code;
}
