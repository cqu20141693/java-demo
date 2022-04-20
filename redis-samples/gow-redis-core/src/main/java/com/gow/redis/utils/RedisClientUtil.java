package com.gow.redis.utils;

import com.gow.redis.operation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author gow
 * @date 2021/6/24
 */
@Component
public class RedisClientUtil {

    @Autowired
    private RedisCommonFacade redisCommonFacade;
    @Autowired
    private RedisStringFacade redisStringFacade;
    @Autowired
    private RedisHashFacade redisHashFacade;
    @Autowired
    private RedisListFacade redisListFacade;
    @Autowired
    private RedisSetFacade redisSetFacade;
    @Autowired
    private RedisZSetFacade redisZSetFacade;
    @Autowired
    private RedisAdvanceFacade redisAdvanceFacade;

    private static final String SEPARATOR = ":";

    public String buildRedisKey(String prefix, String key) {
        return String.join(SEPARATOR, prefix, key);
    }

    public void expire(String prefix, String key, long time) {
        redisCommonFacade.expire(buildRedisKey(prefix, key), time);
    }

    public long getExpire(String prefix, String key) {
        return redisCommonFacade.getExpire(buildRedisKey(prefix, key));
    }

    public void remove(String prefix, String key) {
        redisCommonFacade.del(buildRedisKey(prefix, key));
    }

    public boolean hasKey(String prefix, String key) {
        return redisCommonFacade.hasKey(buildRedisKey(prefix, key));
    }

    public String get(String prefix, String key) {
        return redisStringFacade.get(buildRedisKey(prefix, key));
    }

    public void set(String prefix, String key, String value) {
        redisStringFacade.set(buildRedisKey(prefix, key), value);
    }

    public void set(String prefix, String key, String value, long time, TimeUnit timeUnit) {
        redisStringFacade.setEx(buildRedisKey(prefix, key), value, time, timeUnit);
    }

    public String getAndSet(String prefix, String key, String value) {
        return redisStringFacade.getAndSet(buildRedisKey(prefix, key), value);
    }

    public long size(String prefix, String key) {
        return redisStringFacade.size(buildRedisKey(prefix, key));
    }

    public long incrementBy(String prefix, String key, long delta) {
        return redisStringFacade.incrementBy(buildRedisKey(prefix, key), delta);
    }

    public long decrementBy(String prefix, String key, long delta) {
        return redisStringFacade.decrementBy(buildRedisKey(prefix, key), delta);
    }

    public boolean setIfAbsent(String prefix, String key, String value) {
        return redisStringFacade.setIfAbsent(buildRedisKey(prefix, key), value);
    }

    public List<String> multiGet(String prefix, String... keys) {
        if (keys == null) {
            return null;
        }
        ArrayList<String> strings = new ArrayList<>();
        for (String key : keys) {
            strings.add(buildRedisKey(prefix, key));
        }
        return redisStringFacade.multiGet((String[]) strings.toArray());
    }

    public void multiPut(String prefix, Map<String, String> map) {
        if (map == null || map.size() == 0) {
            return;
        }
        HashMap<String, String> keyValueMap = new HashMap<>();
        map.forEach((k, v) -> keyValueMap.put(prefix + k, v));
        redisStringFacade.multiSet(keyValueMap);
    }

    public void hSet(String prefix, String key, String item, String value) {
        redisHashFacade.hSet(buildRedisKey(prefix, key), item, value);
    }

    public String hGet(String prefix, String key, String item) {
        return redisHashFacade.hGet(buildRedisKey(prefix, key), item);
    }

    public long hIncrementBy(String prefix, String key, String item, long delta) {
        return redisHashFacade.hIncrementBy(buildRedisKey(prefix, key), item, delta);
    }

    public long hDecrementBy(String prefix, String key, String item, long delta) {
        return redisHashFacade.hDecrementBy(buildRedisKey(prefix, key), item, delta);
    }

    public void leftPush(String prefix, String key, String element) {
        redisListFacade.leftPush(buildRedisKey(prefix, key), element);
    }

    public void rightPush(String prefix, String key, String element) {
        redisListFacade.rightPush(buildRedisKey(prefix, key), element);
    }

    public String leftPop(String prefix, String key) {
        return redisListFacade.leftPop(buildRedisKey(prefix, key));
    }

    public long listSize(String prefix, String key) {
        return redisListFacade.size(buildRedisKey(prefix, key));
    }

    public void addSet(String prefix, String key, String element) {
        redisSetFacade.addSet(buildRedisKey(prefix, key), element);
    }

    public boolean setContains(String prefix, String key, String element) {
        return redisSetFacade.isMember(buildRedisKey(prefix, key), element);
    }

    public long setSize(String prefix, String key) {
        return redisSetFacade.size(buildRedisKey(prefix, key));
    }

    public boolean addZSet(String prefix, String key, String value, double score) {
        return redisZSetFacade.zAdd(buildRedisKey(prefix, key), value, score);
    }

    public double getZSetScore(String prefix, String key, String value) {
        return redisZSetFacade.zScore(buildRedisKey(prefix, key), value);
    }

    public double incrementZSetScoreBy(String prefix, String key, String element, double delta) {
        return redisZSetFacade.zIncrBy(buildRedisKey(prefix, key), element, delta);
    }

    public long zSetSize(String prefix, String key) {
        return redisZSetFacade.zCard(buildRedisKey(prefix, key));
    }


    public void addHyperLog(String prefix, String key, String... values) {
        redisAdvanceFacade.pfAdd(buildRedisKey(prefix, key), values);
    }

    public long getHyperLogCount(String prefix, String key) {
        return redisAdvanceFacade.pfCount(buildRedisKey(prefix, key));
    }

    public boolean setBit(String prefix, String key, int offset, boolean flag) {
        return redisAdvanceFacade.setBit(buildRedisKey(prefix, key), offset, flag);
    }

    public boolean getBit(String prefix, String key, int offset) {
        return redisAdvanceFacade.getBit(buildRedisKey(prefix, key), offset);
    }

    public long countBit(String prefix, String key) {
        return redisAdvanceFacade.bitCount(buildRedisKey(prefix, key));
    }

    public List<Object> executePipeline(RedisCallback redisCallback) {
        return redisAdvanceFacade.executePipeline(redisCallback);
    }

}
