package com.gow.redis.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author gow
 * @date 2021/6/24
 */
@Component
public class RedisListFacade extends RedisClient {


    @Autowired
    public RedisListFacade(StringRedisTemplate template) {
        super(template);
    }


    /**
     * 根据 索引获取 list缓存值
     *
     * @param key   键
     * @param start 开始索引（下标从0开始）
     * @param end   偏移量（-1，则遍历全部数据）
     * @return
     */
    public List<String> range(String key, long start, long end) {
        return template.opsForList().range(key, start, end);
    }

    /**
     * 获取 List缓存的长度
     *
     * @param key 键
     * @return
     */
    public long size(String key) {
        return template.opsForList().size(key);
    }

    /**
     * 获取 key键 对应集合中 index索引的值
     *
     * @param key   键
     * @param index 索引
     * @return key键 对应集合中 index索引的值
     */
    public String index(String key, long index) {
        return template.opsForList().index(key, index);
    }

    /**
     * 将 value值放入 key对应的List集合 尾部
     *
     * @param key   键
     * @param value 值
     */
    public void rightPush(String key, String value) {
        template.opsForList().rightPush(key, value);
    }

    /**
     * 将 value值数组放入 key对应的List集合 尾部
     *
     * @param key    键
     * @param values 值数组
     */
    public void rightPush(String key, String... values) {
        template.opsForList().rightPushAll(key, values);
    }

    /**
     * 将 value值放入 key对应的List集合 头部
     *
     * @param key   键
     * @param value 值
     */
    public void leftPush(String key, String value) {
        template.opsForList().leftPush(key, value);
    }

    /**
     * 将 value值数组放入 key对应的List集合 头部
     *
     * @param key    键
     * @param values 值数组
     */
    public void leftPush(String key, String... values) {
        template.opsForList().leftPushAll(key, values);
    }

    /**
     * 修改 key键对应的List集合中 索引为index的值
     *
     * @param key   键
     * @param index 索引
     * @param value 要更改的值
     */
    public void setIndex(String key, long index, String value) {
        template.opsForList().set(key, index, value);
    }

    /**
     * 从List的头部进行取出元素
     *
     * @param key
     * @return
     */
    public String leftPop(String key) {
        return template.opsForList().leftPop(key);
    }

    /**
     * 从List的头部进行取出元素,
     * 如果key不存在且block timeout
     *
     * @param key
     * @param timeout 秒
     * @return
     */
    public String leftBlockPop(String key, Long timeout) {
        return template.opsForList().leftPop(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 从List的头部进行取出元素
     *
     * @param key
     * @return
     */
    public String rightPop(String key) {
        return template.opsForList().rightPop(key);
    }

    /**
     * 从List的尾部进行取出元素,
     * 如果key不存在且block timeout
     *
     * @param key
     * @param timeout 秒
     * @return
     */
    public String rightBlockPop(String key, Long timeout) {
        return template.opsForList().rightPop(key, timeout, TimeUnit.SECONDS);
    }
}
