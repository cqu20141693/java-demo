package com.gow.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author wujt
 */
public class redisZSetUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 时间复杂度 logN ，跳表查询
    public Boolean zadd(String key, String value, double score) {
        Boolean add = stringRedisTemplate.opsForZSet().add(key, value, score);
        return add;
    }

    /**
     * 时间复杂度 MlogN ，跳表查询, M是valueScores个数
     */
    public Long zadds(String key, Map<String, Double> valueScores) {
        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
        valueScores.forEach((value, score) -> tuples.add(new DefaultTypedTuple<>(value, score)));
        return stringRedisTemplate.opsForZSet().add(key, tuples);
    }

    // 时间负责度1； ZSet结构中利用字典存储了对象和分数的映射
    public Double zScore(String key, String member) {
        Double score = stringRedisTemplate.opsForZSet().score(key, member);
        return score;
    }

    /**
     * 时间复杂度，log N, 需要先找到成员
     *
     * @param key
     * @param incr
     * @param member
     * @return
     */
    public Double zIncrBy(String key, double incr, String member) {
        return stringRedisTemplate.opsForZSet().incrementScore(key, member, incr);
    }

    /**
     * 时间复杂度 1；判断key是否存在
     * 当 key 存在且是有序集类型时，返回有序集的基数。 当 key 不存在时，返回 0
     *
     * @param key
     * @return
     */
    public Long zCard(String key) {
        return stringRedisTemplate.opsForZSet().zCard(key);
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
        return stringRedisTemplate.opsForZSet().count(key, min, max);
    }


    public Set<ZSetOperations.TypedTuple<String>> zrevrangeWithScores(String key, long min, long max) {
        return stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, min, max);
    }


    public Set<ZSetOperations.TypedTuple<String>> zrangeByScoreWithScores(String key, double min, double max) {
        return stringRedisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
    }
}
