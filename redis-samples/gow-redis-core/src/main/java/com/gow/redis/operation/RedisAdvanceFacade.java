package com.gow.redis.operation;

import static com.gow.redis.domain.RedisConstant.SCAN_PATTERN;
import com.google.common.base.Preconditions;
import com.gow.redis.support.BloomFilterHelper;
import com.gow.redis.utils.RedisScriptUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/6/24
 */
@Component
public class RedisAdvanceFacade extends RedisClient {

    @Autowired
    private RedisScriptUtils redisScriptUtils;

    @Autowired
    public RedisAdvanceFacade(StringRedisTemplate template) {
        super(template);
    }

    public List<Object> executePipeline(RedisCallback redisCallback) {
        return template.executePipelined(redisCallback);
    }

    /**
     * HyperLogLog  add
     *
     * @param key
     * @param values
     * @return
     */
    public Long pfAdd(String key, String... values) {

        return template.opsForHyperLogLog().add(key, values);
    }

    /**
     * HyperLogLog count
     *
     * @param key
     * @return
     */
    public Long pfCount(String key) {
        return template.opsForHyperLogLog().size(key);
    }

    /**
     * 执行 lua 脚本
     *
     * @param script
     * @param keys
     * @param args
     * @return
     */
    public <T> T execute(RedisScript<T> script, List<String> keys, Object... args) {
        return template.execute(script, keys, args);
    }

    /**
     * 设置位图（Bit) offset 位置标志为flag
     *
     * @param key
     * @param offset
     * @param flag
     * @return
     */
    public boolean setBit(String key, int offset, boolean flag) {
        return template.opsForValue().setBit(key, offset, flag);
    }

    /**
     * 获取位图（Bit) offset 位置标志为flag
     *
     * @param key
     * @param offset
     * @return
     */
    public boolean getBit(String key, int offset) {
        return template.opsForValue().getBit(key, offset);
    }

    /**
     * 获取整个位图（Bit) flag 为true的个数
     *
     * @param key
     * @return
     */
    public long bitCount(String key) {
        return executeAction(conn -> conn.bitCount(key.getBytes()));
    }

    /**
     * 获取位图（Bit) from offset to offset +limit 段中连续flag=true的次数
     *
     * @param key
     * @param limit
     * @param offset
     * @return
     */
    public List<Long> bitField(String key, int limit, int offset) {
        return executeAction(conn -> conn.bitField(key.getBytes(),
                BitFieldSubCommands.create().get(BitFieldSubCommands.BitFieldType.unsigned(limit)).valueAt(offset)));
    }

    /**
     * 根据给定的布隆过滤（BloomFilter）器添加值
     */
    public <T> void bloomSet(BloomFilterHelper<T> bloomFilterHelper, String key, T value) {
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        this.bloomSet(key, offset);
    }

    private void bloomSet(String key, int[] offset) {
        String[] args = new String[offset.length];
        for (int i = 0; i < offset.length; i++) {
            args[i] = String.valueOf(offset[i]);
        }
        template.execute(redisScriptUtils.getBloomSetScript(), Collections.singletonList(key), args);
    }

    /**
     * 根据给定的布隆过滤器（BloomFilter）判断值是否存在
     */
    public <T> boolean bloomFilter(BloomFilterHelper<T> bloomFilterHelper, String key, T value) {
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        return this.bloomFilter(key, offset);
    }

    private Boolean bloomFilter(String key, int[] offset) {
        String[] args = new String[offset.length];
        for (int i = 0; i < offset.length; i++) {
            args[i] = String.valueOf(offset[i]);
        }
        Boolean execute =
                template.execute(redisScriptUtils.getBloomFilterScript(), Collections.singletonList(key), args);
        return execute != null && execute;
    }

    /**
     * scan 实现
     *
     * @param pattern   表达式
     * @param scanCount 每次scan 数量
     * @param consumer  对迭代到的key进行操作
     */
    public void scan(String pattern, long scanCount, Consumer<byte[]> consumer) {
        if (scanCount < 0) {
            return;
        }
        this.executeAction((RedisConnection connection) -> {
            try (Cursor<byte[]> cursor = connection
                    .scan(ScanOptions.scanOptions().count(scanCount).match(pattern).build())) {
                cursor.forEachRemaining(consumer);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 获取符合条件的key
     *
     * @param prefix 表达式
     * @return
     */
    public List<String> keysByScan(String prefix, long scanCount) {
        List<String> keys = new ArrayList<>();
        String pattern = prefix + SCAN_PATTERN;
        this.scan(pattern, scanCount, item -> {
            //符合条件的key
            String key = new String(item, StandardCharsets.UTF_8);
            keys.add(key);
        });
        return keys;
    }

    private <T> T executeAction(RedisCallback<T> action) {
        return template.execute(action);
    }

}
