package com.wujt.并发编程.juc_lock;

import com.wujt.并发编程.util.ConcurrentUtils;

import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLock : since 1.5
 * <p>
 * field:
 * private final ReentrantReadWriteLock.ReadLock readerLock :读锁，主要实现了共享锁的lock,release方法
 * private final ReentrantReadWriteLock.WriteLock writerLock ： 写锁，实现了独占锁的lock,release方法
 * final Sync sync ： 针对读写锁状态的设计和规则实现共享锁和独占锁的获取和释放（state 分段处理；阻塞判断和降级实现）
 * <p>
 * API:
 * <p>
 * 实现原理：
 * 读写状态的设计：
 * 内部通过同一个同步器实现独占写锁和共享读锁，将同步器状态一分为二，高16位读锁状态，低16位写锁状态。
 * 写锁的获取和释放：
 * 获取：利用共享锁的tryAcquire 对state 的低16 位进行状态判断，其中增加了一个是否存在读锁，如果存在读锁；需要等读锁执行完；如果独占锁被占用，如果不是自己；如果读写锁都不存在；此时可以直接强占锁
 * 释放： 和独占锁释放过程一致，只是释放为同步器状态的低16位数据。
 * 阻塞过程和独占锁一致；将阻塞线程放入同步队列中.
 * <p>
 * 读锁实现原理：
 * 获取：如果当前其他线程获取了写锁；则当前线程直接阻塞，如果当前线程获取了写锁或者没有线程获取写锁，则当前线程增加读状态，成功获取读锁。
 * 释放；读锁释放和共享锁释放是一样的，只是对状态字段的高16位进行释放。
 * <p>
 * 锁降级：
 * 在一个线程释放写锁之前，先获取读锁；然后在读取数据；这样就能保证每个线程的修改对其他所有的线程都是可以见的，此时任意线程都可以进行读；因为读是共享的；而其他的线程要进行写需要进行阻塞；等待之前获取到锁的线程执行完后；开始进行写操作。
 * <p>
 * 特性：
 * 公平性
 * 可重入
 * 锁降级
 * <p>
 * 优势：
 * 性能更好，能解决脏读; 并对读多写少的场景支持更好。
 *
 * @author wujt
 */
public class ReentrantReadWriteLockDemo {

    HashMap<String, String> cacheMap = new HashMap<>();
    // 读写锁
    ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
    ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
    ScheduledExecutorService readScheduledExecutorService = new ScheduledThreadPoolExecutor(2);
    ScheduledExecutorService wirteScheduledExecutorService = new ScheduledThreadPoolExecutor(2);

    // 锁降级
    private Boolean update = false;

    public static void main(String[] args) {
        // 测试当前线程存储了获取的锁的数量。释放时如果不对应；将会报错。
        ReentrantReadWriteLockDemo readWriteLockDemo = new ReentrantReadWriteLockDemo();
        try {
            readWriteLockDemo.test();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * 测试读写API和并发性
         */
        testAPI();

        // 测试锁降级
        ReentrantReadWriteLockDemo readWriteLockDemo1 = new ReentrantReadWriteLockDemo();
        readWriteLockDemo1.lockDowngrade();
    }

    private static void testAPI() {
        long currentTimeMillis = System.currentTimeMillis();
        ReentrantReadWriteLockDemo readWriteLockDemo = new ReentrantReadWriteLockDemo();
        // 两个读锁将会同时打印。
        readWriteLockDemo.startRead();
        readWriteLockDemo.startUpdate();

        while (System.currentTimeMillis() - currentTimeMillis < 60000) {
            ConcurrentUtils.sleep(1000);
        }

        readWriteLockDemo.shutDown();
    }

    public void updateCache(String key, String value) {
        writeLock.lock();
        try {
            cacheMap.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    public String getCache(String key) {
        readLock.lock();
        try {
            String value = cacheMap.get(key);

            return value;
        } finally {
            readLock.unlock();
        }
    }

    public void cleanCache() {
        writeLock.lock();
        try {
            cacheMap.clear();
        } finally {
            writeLock.unlock();
        }
    }

    public void startRead() {
        readScheduledExecutorService.scheduleWithFixedDelay(() -> {
            System.out.println("value=" + getCache("taoge"));
        }, 0, 1, TimeUnit.SECONDS);
        readScheduledExecutorService.scheduleWithFixedDelay(() -> {
            System.out.println("value=" + getCache("gongqiong"));
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void startUpdate() {
        wirteScheduledExecutorService.scheduleWithFixedDelay(() -> {
            updateCache("taoge", "nihao");
            updateCache("gongqiong", "zuimei");
        }, 0, 3, TimeUnit.SECONDS);
        wirteScheduledExecutorService.scheduleWithFixedDelay(() -> {
            cleanCache();
        }, 0, 2, TimeUnit.SECONDS);
    }

    public void shutDown() {
        readScheduledExecutorService.shutdown();
        wirteScheduledExecutorService.shutdown();
    }


    /**
     * 实现锁降级
     */
    public void lockDowngrade() {
        // 注意这里的
        readLock.lock();
        try {

            System.out.println("当前线程获取写锁");
            writeLock.lock();
            try {
                System.out.println("准备获取读锁，进行数据操作");
            } finally {
                System.out.println("释放写锁，如果当前线程获取有读锁，锁降级");
                readLock.lock();
                readLock.unlock();
                writeLock.unlock();
            }
            System.out.println("降级读锁操作");
        } finally {
            System.out.println("释放读锁");
            readLock.unlock();
        }

    }

    public void test() {
        readLock.unlock();
    }
}
