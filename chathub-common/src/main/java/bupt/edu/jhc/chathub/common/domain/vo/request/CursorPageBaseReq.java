package bupt.edu.jhc.chathub.common.domain.vo.request;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description: 游标分页基础请求
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/18
 */
@Data
@Accessors(chain = true)
@ApiModel("游标分页基础请求")
public class CursorPageBaseReq {
    @ApiModelProperty("每页大小")
    private Integer pageSize = 20; // 每页大小默认 20
    @ApiModelProperty("游标")
    private String cursor; // 游标

    @JsonIgnore
    public Boolean isFirstPage() {
        return StrUtil.isBlank(cursor);
    }
}
