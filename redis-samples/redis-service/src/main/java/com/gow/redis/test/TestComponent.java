package com.gow.redis.test;

import com.alibaba.fastjson.JSONObject;
import com.gow.redis.operation.RedisMQFacade;
import com.gow.redis.utils.RedisClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

import static com.gow.redis.domain.CacheDomainEnum.USER_CACHE;

/**
 * @author gow
 * @date 2021/6/24
 */
@Component
@Slf4j
public class TestComponent implements InitializingBean {

    @Autowired
    private RedisClientUtil redisClientUtil;

    @Autowired
    private RedisMQFacade redisMQFacade;

    @Override
    public void afterPropertiesSet() throws Exception {

        testCommon();

        testString();
        testHash();
        testList();
        testSet();
        testZSet();
        testAdvance();

        testMQ();

        //todo test Pipeline
        // todo testRedisson
    }

    private void testMQ() {
        log.info("test MQ start");
        String topic = "D:D:deviceKey";
        String message = "data";
        redisMQFacade.addPatternTopic(topic);
        redisMQFacade.publish(topic, message);

        log.info("stream test");

        String streamKey = "my-stream";
        String group = "my-group";
        try {
            redisMQFacade.createGroup(streamKey, group);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("key1", "message1");
        StringRecord record = StreamRecords.string(map).withStreamKey(streamKey);
        redisMQFacade.appendRecord(record);

        redisMQFacade.consumer(Consumer.from(group, "my-consumer"),
                StreamReadOptions.empty().count(2),
                StreamOffset.create(streamKey, ReadOffset.lastConsumed()), (msg) -> {
                    log.info("message={}", JSONObject.toJSONString(msg));

                });

        log.info("test MQ end");
    }

    private void testAdvance() {
        log.info("test advance start");
        String hyper = "hyper";
        String hyperValue = "hyper";
        String bit = "bit";
        int offset = 10;
        String prefix = USER_CACHE.getPrefix();
        redisClientUtil.addHyperLog(prefix, hyper, hyperValue);
        log.info("hyper get={}, size={}", redisClientUtil.getBit(prefix, hyper, offset), redisClientUtil.getHyperLogCount(prefix, hyper));

        log.info("bit size={},set={},size={}", redisClientUtil.countBit(prefix, bit),
                redisClientUtil.setBit(prefix, bit, offset, true), redisClientUtil.countBit(prefix, bit));
        double batchSize = 100;

        log.info("test redis pipeline");
        List<Object> list = redisClientUtil.executePipeline((RedisCallback<Object>) connection -> {
            StringRedisConnection stringRedisConn = (StringRedisConnection) connection;
            String key = "list";
            for (int i = 0; i < batchSize; i++) {
                stringRedisConn.rPop(String.join(":", prefix, key));
            }
            //Note that the value returned from the RedisCallback is required to be null,
            // as this value is discarded in favor of returning the results of the pipelined commands.
            return null;
        });
        log.info("test redis pipeline result={}", JSONObject.toJSON(list));


        log.info("test advance end");

    }

    private void testZSet() {
        log.info("test zSet start");
        double score = 10;
        String value = "value";
        String key = "zSet";
        String prefix = USER_CACHE.getPrefix();
        boolean addZSet = redisClientUtil.addZSet(prefix, key, value, score);
        log.info("zSet score={}", redisClientUtil.getZSetScore(prefix, key, value));
        redisClientUtil.incrementZSetScoreBy(prefix, key, value, 10);
        log.info("zSet score={},zSetSize={}", redisClientUtil.getZSetScore(prefix, key, value), redisClientUtil.zSetSize(prefix, key));
        log.info("test zSet end");
    }

    private void testSet() {
        log.info("test set start");
        String value = "left";
        String key = "set";
        String prefix = USER_CACHE.getPrefix();

        redisClientUtil.addSet(prefix, key, value);
        log.info("set contains value={} is {},size={} ", value,
                redisClientUtil.setContains(prefix, key, value), redisClientUtil.setSize(prefix, key));
        log.info("test set end");
    }

    private void testList() {
        log.info("test list start");
        String left = "left";
        String right = "right";
        String key = "list";
        String prefix = USER_CACHE.getPrefix();

        redisClientUtil.rightPush(prefix, key, right);
        redisClientUtil.leftPush(prefix, key, left);
        log.info("size={},left pop value={},size={}", redisClientUtil.listSize(prefix, key),
                redisClientUtil.leftPop(prefix, key), redisClientUtil.listSize(prefix, key));

        log.info("test list end");

    }

    private void testHash() {
        log.info("test hash start");
        String item = "item";
        String key = "hash";
        String prefix = USER_CACHE.getPrefix();


        String value = redisClientUtil.hGet(prefix, key, item);
        if (value == null) {
            redisClientUtil.hSet(prefix, key, item, "0");
            log.info("add value={}", redisClientUtil.hGet(prefix, key, item));
        } else {
            log.info("get value={}", value);
        }
        log.info("increment value={}", redisClientUtil.hIncrementBy(prefix, key, item, 1L));
        log.info("decrement value={}", redisClientUtil.hDecrementBy(prefix, key, item, 1L));

        log.info("test hash end");

    }

    private void testString() {
        log.info("test String start");
        String key = "name";
        String prefix = USER_CACHE.getPrefix();


        String value = redisClientUtil.get(prefix, key);
        if (value == null) {
            redisClientUtil.set(prefix, key, "0");
            log.info("add value={}", redisClientUtil.get(prefix, key));
        } else {
            log.info("get value={}", value);
        }
        log.info("increment value={}", redisClientUtil.incrementBy(prefix, key, 1L));
        log.info("decrement value={}", redisClientUtil.decrementBy(prefix, key, 1L));

        log.info("test String end");
    }

    private void testCommon() {
        log.info("test common start");
        String key = "name";
        String prefix = USER_CACHE.getPrefix();
        redisClientUtil.set(prefix, key, "gow");
        redisClientUtil.expire(prefix, key, 30L);

        long expire = redisClientUtil.getExpire(prefix, key);
        boolean hasKey = redisClientUtil.hasKey(prefix, key);
        log.info("prefix={},key={},ttl={},hasKey={}", prefix, key, expire, hasKey);

        redisClientUtil.remove(prefix, key);

        // add other method test

        log.info("test common end");
    }
}
