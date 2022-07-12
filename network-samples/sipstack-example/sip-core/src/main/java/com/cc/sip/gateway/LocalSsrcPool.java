package com.cc.sip.gateway;

import com.cc.gb28181.SsrcPool;
import lombok.Data;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 本地ssrc pool
 * wcc 2022/6/23
 */
@Data
public class LocalSsrcPool implements SsrcPool {

    private final Integer poolSize;
    private final String prefix = "local";
    //底层基于链表的无界队列，使用CAS来保证并发安全，因为没有使用锁，所以size()方法可能不准确，因为可能同时做了入队和出队操作。
    private final ConcurrentLinkedQueue<String> pool;

    public LocalSsrcPool() {
        this(10000);
    }

    public LocalSsrcPool(Integer poolSize) {
        this.poolSize = poolSize;
        pool = new ConcurrentLinkedQueue<>();
        initPool();
    }

    private void initPool() {

        //初始化可用到ssrc到redis
        for (int i = 1; i < poolSize; i++) {
            pool.add(String.format("%04d", i));
        }
    }

    @Override
    public String generatePlaySsrc() {
        return Optional.ofNullable(pool.poll()).map(ssrc -> "0" + prefix + ssrc).orElseThrow(() -> new RuntimeException("ssrc pool exhaust"));

    }

    @Override
    public String generatePlayBackSsrc() {
        return Optional.ofNullable(pool.poll()).map(ssrc -> "1" + prefix + ssrc).orElseThrow(() -> new RuntimeException("ssrc pool exhaust"));
    }

    @Override
    public void release(String ssrc) {
        pool.add(ssrc.substring(prefix.length() + 1));
    }
}
