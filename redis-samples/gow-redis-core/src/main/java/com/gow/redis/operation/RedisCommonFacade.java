package com.gow.redis.operation;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @author wujt  2021/5/14
 */
@Component
@Slf4j
public class RedisCommonFacade extends RedisClient {

    @Autowired
    public RedisCommonFacade(StringRedisTemplate template) {
        super(template);
    }

    /**
     * 设置key过期时间
     *
     * @param key  key
     * @param time (秒)
     * @date 2021/5/14 10:40
     */
    public void expire(String key, long time) {
        template.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 设置key在某个时间点过期
     *
     * @param key  key
     * @param date 日期
     */
    public void expireAt(String key, Date date) {
        template.expireAt(key, date);
    }

    /**
     * 取 key键 的失效时间（ttl key）
     *
     * @param key 键
     * @return 失效时间（单位：秒）
     */
    public long getExpire(String key) {

        return Optional.ofNullable(template.getExpire(key, TimeUnit.SECONDS)).orElse(-1L);
    }

    /**
     * 删除key
     *
     * @param key key
     */
    public void del(String key) {
        template.delete(key);
    }

    /**
     * 按照 key值前缀 批量删除 缓存
     *
     * @param prefix key值前缀
     */
    public void delByPrefix(String prefix) {
        Set<String> keys = template.keys(prefix);
        if (!CollectionUtils.isEmpty(keys)) {
            template.delete(keys);
        }
    }

    /**
     * 批量删除key
     *
     * @param keys keys
     */
    public void delKeys(Set<String> keys) {
        template.delete(keys);

    }

    /**
     * 判断 key键 是否存在（exists key）
     *
     * @param key 键
     * @return 存在：true；不存在：false
     */
    public boolean hasKey(String key) {
        return template.hasKey(key);
    }


}
