package com.wujt.并发编程.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wujt
 */
public class MyThreadFactory implements ThreadFactory {
    /**
     * 工厂计数器
     */
    private AtomicInteger counter;
    /**
     * 工厂名称
     */
    private String name;

    public MyThreadFactory(AtomicInteger counter, String name) {
        this.counter = counter;
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, name + counter.incrementAndGet());
    }
}
