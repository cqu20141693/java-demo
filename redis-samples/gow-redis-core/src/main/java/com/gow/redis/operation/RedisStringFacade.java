package com.gow.redis.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author gow
 * @date 2021/6/24
 */
@Component
public class RedisStringFacade extends RedisClient {


    @Autowired
    public RedisStringFacade(StringRedisTemplate template) {
        super(template);
    }


    /**
     * 获取String vale
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return template.opsForValue().get(key);
    }

    /**
     * 设置 key value
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        template.opsForValue().set(key, value);
    }

    /**
     * set and expire(setEx)
     *
     * @param key
     * @param value
     * @param time
     * @param timeUnit
     */
    public void setEx(String key, String value, long time, TimeUnit timeUnit) {
        template.opsForValue().set(key, value, time, timeUnit);
    }
    /**
     * 如果键不存在则新增,存在则不改变已经有的值（setnx key value）
     *
     * @param key   键
     * @param value 值
     * @return key键不存在，返回ture；存在返回false
     */
    public boolean setIfAbsent(String key, String value) {
        return template.opsForValue().setIfAbsent(key, value);
    }
    /**
     * 在原有的值基础上新增字符串到末尾（append key value）
     *
     * @param key   键
     * @param value 新增的字符串
     */
    public void append(String key, String value) {
        template.opsForValue().append(key, value);
    }

    /**
     * 截取key键对应值得字符串，从开始下标位置到结束下标位置(包含)的字符串（getrange key start end）
     *
     * @param key   键
     * @param start 开始下标位置
     * @param end   结束下标的位置
     * @return 从开始下标位置到结束下标位置(包含)的字符串
     */
    public String get(String key, long start, long end) {
        return template.opsForValue().get(key, start, end);
    }

    /**
     * 获取原来key键对应的值并重新赋新值（getset key value）
     *
     * @param key   键
     * @param value 新值
     * @return 旧值（原来key键对应的值）
     */
    public String getAndSet(String key, String value) {
        return template.opsForValue().getAndSet(key, value);
    }

    /**
     * 获取指定key键对应值的长度（String）
     *
     * @param key 键
     * @return 返回对应值的长度
     */
    public long size(String key) {
        return template.opsForValue().size(key);
    }

    /**
     * 将存储在key键上的数字按指定的值 增加（incrby key number）
     *
     * @param key   键
     * @param delta 指定的增加数字
     * @return 返回增加后的值（key键不存在，则在执行操作之前将其设置为0）
     */
    public long incrementBy(String key, long delta) {
        return template.opsForValue().increment(key, delta);
    }

    /**
     * 将存储在key键上的数字按指定的值 减少（incrby key number）
     *
     * @param key   键
     * @param delta 指定的减少数字
     * @return 返回减少后的值（key键不存在，则在执行操作之前将其设置为0）
     */
    public long decrementBy(String key, long delta) {
        return template.opsForValue().increment(key, -delta);
    }


    /**
     * 一次多个键设置它们的值（mset key1 value1 key2 value2 ..）
     *
     * @param keyValueMap key为键，value为值
     */
    public void multiSet(Map<String, String> keyValueMap) {
        template.opsForValue().multiSet(keyValueMap);
    }

    /**
     * 根据键数组取出对应的value值（mget key1 key2 ..）
     *
     * @param keys 键数组
     * @return
     */
    public List<String> multiGet(String... keys) {
        return Optional.ofNullable(keys).map(value -> template.opsForValue().multiGet(Arrays.asList(keys))).orElse(null);
    }


}
