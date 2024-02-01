package bupt.edu.jhc.chathub.common.utils;

import bupt.edu.jhc.chathub.common.domain.vo.request.CursorPageBaseReq;
import bupt.edu.jhc.chathub.common.domain.vo.resp.CursorPageBaseResp;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * @Description: 游标翻页工具类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/18
 */
public class CursorUtils {
    public static <T> CursorPageBaseResp<T> getCursorPageByMysql(IService<T> mapper, CursorPageBaseReq req,
                                                                 Consumer<LambdaQueryWrapper<T>> initWrapper, SFunction<T, ?> cursorColumn) {
        // 1. 初始化 wrapper
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        initWrapper.accept(wrapper);
        if (!req.isFirstPage()) {
            wrapper.lt(cursorColumn, req.getCursor());
        }

        // 2. 排序
        wrapper.orderByDesc(cursorColumn);
        Page<T> page = mapper.page(new Page<T>(1, req.getPageSize()).setSearchCount(false), wrapper);
        if (page.getRecords().isEmpty()) {
            return CursorPageBaseResp.empty();
        }

        // 3. 获取游标
        String cursor = Optional.ofNullable(page.getRecords().get(page.getRecords().size() - 1))
                .map(cursorColumn)
                .map(String::valueOf)
                .orElse(null);
        Boolean isLast = page.getRecords().size() != req.getPageSize();
        return new CursorPageBaseResp<>(isLast, cursor, page.getRecords());
    }
}
