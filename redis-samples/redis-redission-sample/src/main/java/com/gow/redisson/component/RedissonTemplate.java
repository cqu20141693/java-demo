package com.gow.redisson.component;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author wujt  2021/5/14
 */
@Component
@Slf4j
public class RedissonTemplate {

    @Autowired
    private RedissonClient redissonClient;

    private void testApi() {
        RLock lock = redissonClient.getLock("lock-1");
        try {
            boolean tryLock = lock.tryLock(6, 5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lock.isHeldByCurrentThread()) {   // 获取锁的线程才能解锁
                lock.unlock();
            } else {
                // 没有获得锁的处理
            }
        }
    }

    public void getLock(String key) {
        RLock lock = redissonClient.getLock(key);
        try {
            boolean tryLock = lock.tryLock(6, 5, TimeUnit.SECONDS);
            log.info("invoke getLock key={}, tyyLock={}", key, tryLock);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 获取锁的线程才能解锁
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            } else {
                // 没有获得锁的处理
            }
        }
    }
}
