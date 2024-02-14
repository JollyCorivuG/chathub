package bupt.edu.jhc.chathub.common.service.cache;

import bupt.edu.jhc.chathub.common.utils.RedisUtils;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.data.util.Pair;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: redis 缓存抽象类
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/2/11
 */
public abstract class AbstractRedisStringCache<IN, OUT> implements BatchCache<IN, OUT> {
    private final Class<OUT> outClass;

    /**
     * 用于解析出 OUT 的类型
     */
    @SuppressWarnings("unchecked")
    protected AbstractRedisStringCache() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.outClass = (Class<OUT>) genericSuperclass.getActualTypeArguments()[1];
    }

    /**
     * 获取缓存的 key
     * @param req
     * @return String
     */
    protected abstract String getKey(IN req);

    /**
     * 获取缓存的过期时间(秒)
     * @return Long
     */
    protected abstract Long getExpireSeconds();

    /**
     * 从数据库中加载数据到缓存
     * @param req
     * @return Map<IN, OUT>
     */
    protected abstract Map<IN, OUT> load(List<IN> req);

    @Override
    public OUT get(IN req) {
        return getBatch(Collections.singletonList(req)).get(req);
    }

    @Override
    public Map<IN, OUT> getBatch(List<IN> req) {
        if (CollectionUtil.isEmpty(req)) { // 防御性编程
            return new HashMap<>();
        }
        // 1.组装 keys 以及批量 get
        req = req.stream().distinct().collect(Collectors.toList());
        List<String> keys = req.stream().map(this::getKey).collect(Collectors.toList());
        List<OUT> valueList = RedisUtils.mget(keys, outClass);

        // 2.计算差集
        List<IN> loadReqs = new ArrayList<>();
        for (int i = 0; i < valueList.size(); i++) {
            if (Objects.isNull(valueList.get(i))) {
                loadReqs.add(req.get(i));
            }
        }

        // 3.重新加载不足的数据到 redis
        Map<IN, OUT> load = new HashMap<>();
        if (CollectionUtil.isNotEmpty(loadReqs)) { // 批量 load
            load = this.load(loadReqs);
            Map<String, OUT> loadMap = load.entrySet().stream()
                    .map(a -> Pair.of(getKey(a.getKey()), a.getValue()))
                    .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
            RedisUtils.mset(loadMap, getExpireSeconds());
        }

        // 4.组装最后的结果
        Map<IN, OUT> resultMap = new HashMap<>();
        for (int i = 0; i < req.size(); i++) {
            IN in = req.get(i);
            OUT out = Optional.ofNullable(valueList.get(i))
                    .orElse(load.get(in));
            resultMap.put(in, out);
        }
        return resultMap;
    }

    @Override
    public void del(IN req) {
        delBatch(Collections.singletonList(req));
    }

    @Override
    public void delBatch(List<IN> req) {
        List<String> keys = req.stream().map(this::getKey).collect(Collectors.toList());
        RedisUtils.del(keys);
    }
}
