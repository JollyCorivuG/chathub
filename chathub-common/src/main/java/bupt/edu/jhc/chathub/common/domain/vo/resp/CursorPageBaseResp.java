package bupt.edu.jhc.chathub.common.domain.vo.resp;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 游标分页响应类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/18
 */
@Data
@Accessors(chain = true)
@ApiModel("游标分页响应")
@AllArgsConstructor
@NoArgsConstructor
public class CursorPageBaseResp<T> {
    @ApiModelProperty("是否最后一页")
    private Boolean isLast = Boolean.FALSE; // 是否最后一页
    @ApiModelProperty("游标 (下次翻页时需要带上的参数)")
    private String cursor; // 游标 (下次翻页时需要带上的参数)
    @ApiModelProperty("数据列表")
    private List<T> list; // 数据列表

    public static <T> CursorPageBaseResp<T> change(CursorPageBaseResp<?> cursorPage, List<T> list) {
        CursorPageBaseResp<T> cursorPageBaseResp = new CursorPageBaseResp<>();
        cursorPageBaseResp
                .setIsLast(cursorPage.getIsLast())
                .setList(list)
                .setCursor(cursorPage.getCursor());
        return cursorPageBaseResp;
    }

    @JsonIgnore
    public Boolean isEmpty() {
        return CollectionUtil.isEmpty(list);
    }

    public static <T> CursorPageBaseResp<T> empty() {
        CursorPageBaseResp<T> cursorPageBaseResp = new CursorPageBaseResp<>();
        cursorPageBaseResp
                .setIsLast(true)
                .setList(new ArrayList<>());
        return cursorPageBaseResp;
    }
}
