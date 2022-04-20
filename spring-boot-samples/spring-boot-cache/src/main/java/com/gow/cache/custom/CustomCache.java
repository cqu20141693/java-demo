package com.gow.cache.custom;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.RemovalCause;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/12 0012
 */
@Component
@Slf4j
public class CustomCache {

    private CacheCleaner cacheCleaner;
    /**
     * cmd -> execWaitTime
     */
    private Cache<CustomCacheKey, Long> cmdExecCacheMap;

    public CustomCache(CacheCleaner cacheCleaner) {
        this.cacheCleaner = cacheCleaner;
    }

    @PostConstruct
    public void init() {
        // custom expiry strategy
        cmdExecCacheMap = Caffeine.newBuilder()
               // .expireAfterWrite(1, TimeUnit.DAYS)
                // custom expiry strategy by key or value
                .expireAfter(new Expiry<CustomCacheKey, Long>() {

                    @Override
                    public long expireAfterCreate(@NonNull CustomCacheKey key, @NonNull Long value,
                                                  long currentTime) {
                        return value;
                    }

                    @Override
                    public long expireAfterUpdate(@NonNull CustomCacheKey key, @NonNull Long value, long currentTime,
                                                  @NonNegative long currentDuration) {
                        return currentDuration;
                    }

                    @Override
                    public long expireAfterRead(@NonNull CustomCacheKey key, @NonNull Long value, long currentTime,
                                                @NonNegative long currentDuration) {
                        return currentDuration;
                    }
                })
                .removalListener((key, value, removalCause) -> {
                    // handle RemovalCause
                    if (removalCause == RemovalCause.EXPIRED && key != null) {
                        log.info("cache key expired. cacheKey = {},  value={}", key, value);
                    } else if (removalCause == RemovalCause.EXPLICIT) {
                        log.info("cache key was manually removed by the user. cacheKey = {},  value={}", key, value);
                    } else if (removalCause == RemovalCause.COLLECTED) {
                        log.info("cache key or value was garbage-collected. cacheKey = {},  value={}", key, value);
                    } else if (removalCause == RemovalCause.SIZE) {
                        log.info("he entry was evicted due to size constraints. cacheKey = {},  value={}", key, value);
                    }
                })
                .maximumSize(1000000)
                .build();
        // 每分钟在后台做一次缓存清理,只有清理数据时才会出发removal
        cacheCleaner.registerCycleCleanJob(cmdExecCacheMap, 3);
    }

    public void add(String groupKey, String sn, String cmdTag, long execWaitTime) {
        CustomCacheKey key = new CustomCacheKey();
        key.setGroupKey(groupKey).setSn(sn).setCmdTag(cmdTag);

        cmdExecCacheMap.put(key, execWaitTime);
    }

    public void remove(String groupKey, String sn, String cmdTag) {
        CustomCacheKey key = new CustomCacheKey();
        key.setGroupKey(groupKey).setSn(sn).setCmdTag(cmdTag);
        // 删除过期策略
        cmdExecCacheMap.invalidate(key);
    }
}
