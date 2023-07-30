package com.jhc.chathub.pojo.dto.message;

import lombok.Data;
import lombok.experimental.Accessors;
@Data
@Accessors(chain = true)
public class WSReqDTO {
    Integer type;
    String data;
}
