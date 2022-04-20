package com.gow.redis.operation;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/6/24
 */
@Component
public class RedisSetFacade extends RedisClient {


    @Autowired
    public RedisSetFacade(StringRedisTemplate template) {
        super(template);
    }

    /**
     * 将数据放在 Set缓存
     *
     * @param key    键
     * @param values 值数组
     * @return 成功个数
     */
    public long addSet(String key, String... values) {
        return Optional.ofNullable(values).map(v -> template.opsForSet().add(key, values)).orElse(0L);
    }

    /**
     * 获取 key键 对应的 Set集合
     *
     * @param key 键
     * @return key键 对应的 Set集合
     */
    public Set<String> members(String key) {
        return template.opsForSet().members(key);
    }

    /**
     * 查找 key键 对应的Set集合中 是否包含value值
     *
     * @param key   键
     * @param value 值
     * @return 包含：true；不包含：false
     */
    public boolean isMember(String key, Object value) {
        return template.opsForSet().isMember(key, value);
    }

    /**
     * 移除 key键 对应的Set集合中 value数组
     *
     * @param key    键
     * @param values 要移除的值数组
     * @return 移除成功的个数
     */
    public long remove(String key, String... values) {
        return template.opsForSet().remove(key, values);
    }


    /**
     * 获取 key键 对应的Set集合的长度
     *
     * @param key 键
     * @return
     */
    public long size(String key) {
        return template.opsForSet().size(key);
    }

    public Set<String> sDiff(String key, Collection<String> collection) {
        return template.opsForSet().difference(key, collection);
    }

    public Long sDiffStore(Collection<String> keys, String destKey) {
        return template.opsForSet().differenceAndStore(keys, destKey);
    }
}
