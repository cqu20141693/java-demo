package com.gow.cache.config;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gow
 * @date 2021/7/3 0003
 */
@Configuration
public class CaffeineConfig {

    private final Set<String> names = Stream.of("cacheOne", "cacheTwo").collect(Collectors.toSet());

    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES);
    }

    @Bean("selfManager")
    public CacheManager selfManager(ObjectProvider<CacheLoader<Object, Object>> cacheLoader) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeineConfig());
        cacheLoader.ifAvailable(caffeineCacheManager::setCacheLoader);
        caffeineCacheManager.setCacheNames(names);
        caffeineCacheManager.setAllowNullValues(true);
        return caffeineCacheManager;
    }
}
