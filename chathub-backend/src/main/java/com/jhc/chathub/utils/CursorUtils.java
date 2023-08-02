package com.jhc.chathub.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.chathub.common.request.CursorPageBaseReq;
import com.jhc.chathub.common.resp.CursorPageBaseResp;

import java.util.Optional;
import java.util.function.Consumer;

public class CursorUtils {
    public static <T> CursorPageBaseResp<T> getCursorPageByMysql(IService<T> mapper, CursorPageBaseReq req,
                                                                 Consumer<LambdaQueryWrapper<T>> initWrapper, SFunction<T, ?> cursorColumn) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        initWrapper.accept(wrapper);
        if (!req.isFirstPage()) {
            wrapper.lt(cursorColumn, req.getCursor());
        }
        wrapper.orderByDesc(cursorColumn);
        Page<T> page = mapper.page(new Page<T>(1, req.getPageSize()).setSearchCount(false), wrapper);
        if (page.getRecords().isEmpty()) {
            return CursorPageBaseResp.empty();
        }
        String cursor = Optional.ofNullable(page.getRecords().get(page.getRecords().size() - 1))
                .map(cursorColumn)
                .map(String::valueOf)
                .orElse(null);
        Boolean isLast = page.getRecords().size() != req.getPageSize();
        return new CursorPageBaseResp<>(cursor, isLast, page.getRecords());
    }
}
