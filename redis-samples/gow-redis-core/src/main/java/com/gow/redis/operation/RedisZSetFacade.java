package com.gow.redis.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author wujt  2021/5/10
 */
@Component
public class RedisZSetFacade extends RedisClient {

    @Autowired
    public RedisZSetFacade(StringRedisTemplate template) {
        super(template);
    }


    /**
     * ZSet add
     * 时间复杂度 logN ，跳表查询
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    public Boolean zAdd(String key, String value, double score) {
        Boolean add = template.opsForZSet().add(key, value, score);
        return add;
    }

    /**
     * 时间复杂度 MlogN ，跳表查询, M是valueScores个数
     */
    public Long zBatchAdd(String key, Map<String, Double> valueScores) {
        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
        valueScores.forEach((value, score) -> tuples.add(new DefaultTypedTuple<>(value, score)));
        return template.opsForZSet().add(key, tuples);
    }

    // 时间复杂度1;ZSet结构中利用字典存储了对象和分数的映射
    public Double zScore(String key, String member) {
        Double score = template.opsForZSet().score(key, member);
        return score;
    }

    /**
     * 时间复杂度，log N, 需要先找到成员
     *
     * @param key
     * @param member
     * @param delta
     * @return
     */
    public double zIncrBy(String key, String member, double delta) {
        return template.opsForZSet().incrementScore(key, member, delta);
    }

    /**
     * 时间复杂度 1；判断key是否存在
     * 当 key 存在且是有序集类型时，返回有序集的基数。 当 key 不存在时，返回 0
     *
     * @param key
     * @return
     */
    public long zCard(String key) {
        return template.opsForZSet().zCard(key);
    }

    /**
     * 获取
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long zCount(String key, double min, double max) {
        return template.opsForZSet().count(key, min, max);
    }


    public Set<ZSetOperations.TypedTuple<String>> zRevrangeWithScores(String key, long min, long max) {
        return template.opsForZSet().reverseRangeWithScores(key, min, max);
    }


    public Set<ZSetOperations.TypedTuple<String>> zRangeByScoreWithScores(String key, double min, double max) {
        return template.opsForZSet().rangeByScoreWithScores(key, min, max);
    }
}
