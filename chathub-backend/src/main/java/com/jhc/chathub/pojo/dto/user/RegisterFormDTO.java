package com.jhc.chathub.pojo.dto.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RegisterFormDTO {
    private String phone;
    private String account;
    private String password;
}
