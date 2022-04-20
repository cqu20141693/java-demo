package com.wujt.disruptor.sample;

import com.wujt.disruptor.DisruptorQueue;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author wujt  2021/5/8
 */
@Slf4j
public class DisruptorProducer implements Runnable {
    private String name;
    private DisruptorQueue<Log> disruptorQueue;
    private volatile boolean flag = true;
    private static AtomicInteger count = new AtomicInteger();

    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public DisruptorProducer(String name, DisruptorQueue<Log> disruptorQueue) {
        this.name = name;
        this.disruptorQueue = disruptorQueue;
    }

    @Override
    public void run() {
        try {
            log.info("{} {}:DisruptorProducer启动", now(), this.name);
            while (true) {
                Log data = new Log(count.incrementAndGet(), now());
                // 将数据存入队列中
                readWriteLock.readLock().lock();
                if (!flag) {
                    break;
                }
                try {
                    disruptorQueue.add(data);
                    log.info("{}:DisruptorProducer 生产 {} 数据到队列中", this.name, data);
                } finally {
                    readWriteLock.readLock().unlock();
                }

            }
        } catch (Exception e) {
            log.error("{}:DisruptorProducer occur exception={}", this.name, e.getMessage());
        } finally {
            log.info("{} {}:DisruptorProducer 退出", now(), this.name);
        }
    }

    public void stopThread() {
        readWriteLock.writeLock().lock();
        try {
            this.flag = false;
        } finally {
            readWriteLock.writeLock().unlock();
        }

    }

    /**
     * 获取当前时间（分:秒）
     */
    public String now() {
        LocalDateTime dateTime = LocalDateTime.now();
        return "[" + dateTime.getMinute() + ":" + dateTime.getSecond() + "] ";
    }
}
