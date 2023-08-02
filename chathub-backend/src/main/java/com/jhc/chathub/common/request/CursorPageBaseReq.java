package com.jhc.chathub.common.request;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CursorPageBaseReq {
    private Integer pageSize = 20; // 每页大小
    private String cursor; // 游标

    @JsonIgnore
    public Boolean isFirstPage() {
        return StrUtil.isBlank(cursor);
    }
}
