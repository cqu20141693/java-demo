package com.gow.redis.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author gow
 * @date 2021/6/24
 */
@Component
public class RedisHashFacade extends RedisClient {


    @Autowired
    public RedisHashFacade(StringRedisTemplate template) {
        super(template);
    }

    /**
     * 将hash表中放入数据,如果不存在将创建（hSet key item value）
     *
     * @param key   redis key
     * @param item  hash key
     * @param value hash value
     */
    public void hSet(String key, String item, String value) {
        template.opsForHash().put(key, item, value);
    }

    /**
     * 将 map 表中放入数据,如果不存在将创建（hBatchSet key item1 value1 item2 value2 item3 value3 ...）
     *
     * @param key  redis key
     * @param hash map 表中放入数据
     */
    public void hBatchSet(String key, Map<String, String> hash) {
        template.opsForHash().putAll(key, hash);
    }

    /**
     * 判断是否存在 hash item（hexists key item）
     *
     * @param key  redis key
     * @param item hash item
     * @return 存在返回true，不存在返回 false
     */
    public boolean hasKey(String key, String item) {
        return template.opsForHash().hasKey(key, item);
    }

    /**
     * 获取 hash item 对应的 value（hGet key num）
     *
     * @param key  redis key
     * @param item hash item
     * @return item 对应的 value
     */
    public String hGet(String key, String item) {
        return (String) template.opsForHash().get(key, item);
    }

    /**
     * 获取 key 对应的 hash 数据（hGetAll key）
     *
     * @param key redis key
     * @return 对应的 hash 数据
     */
    public Map<Object, Object> hGetAll(String key) {
        return template.opsForHash().entries(key);
    }

    /**
     * 删除 hash item 对应的项（hRemove key item ...）
     *
     * @param key  redis key
     * @param item hash item
     * @return 返回删除个数
     */
    public long hRemove(String key, String... item) {
        if (null != item && item.length > 0) {
            return template.opsForHash().delete(key, item);
        }
        return 0;
    }

    /**
     * 递增，如果不存在,就会创建一个 并把新增后的值返回（hIncrementBy key item by）
     *
     * @param key   redis key
     * @param item  hash item
     * @param delta 递增量
     * @return 自增后的数量
     */
    public long hIncrementBy(String key, String item, long delta) {
        return template.opsForHash().increment(key, item, delta);
    }

    /**
     * 递减，如果不存在,就会创建一个 并把递减后的值返回（hDecrementBy key item delta）
     *
     * @param key   redis key
     * @param item  hash item
     * @param delta 递减量
     * @return 递减后的数量
     */
    public long hDecrementBy(String key, String item, long delta) {
        return template.opsForHash().increment(key, item, -delta);
    }

    /**
     * 获取 key 对应的 hash item 集合（hkeys key）
     *
     * @param key redis key
     * @return hash item 集合
     */
    public Set<Object> hItemKeys(String key) {
        return template.opsForHash().keys(key);
    }

    /**
     * 获取 key 对应的 hash value 集合（hvals key）
     *
     * @param key redis key
     * @return hash value 集合
     */
    public List<Object> hItemValues(String key) {
        return template.opsForHash().values(key);
    }

    /**
     * 获取 key 对应的 hash 中元素个数（hlen key）
     *
     * @param key
     * @return hash 中元素个数
     */
    public long hSize(String key) {
        return template.opsForHash().size(key);
    }

}
