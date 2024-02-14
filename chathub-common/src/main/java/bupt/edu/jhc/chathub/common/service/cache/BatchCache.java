package bupt.edu.jhc.chathub.common.service.cache;

import java.util.List;
import java.util.Map;

/**
 * @Description: 批量缓存接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/2/11
 */
public interface BatchCache<IN, OUT> {
    /**
     * 获取单个
     * @param req
     * @return OUT
     */
    OUT get(IN req);

    /**
     * 获取批量
     * @param req
     * @return Map<IN, OUT>
     */
    Map<IN, OUT> getBatch(List<IN> req);

    /**
     * 删除单个
     * @param req
     */
    void del(IN req);

    /**
     * 删除多个
     * @param req
     */
    void delBatch(List<IN> req);
}
