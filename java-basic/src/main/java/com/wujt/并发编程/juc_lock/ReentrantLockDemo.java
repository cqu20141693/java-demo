package com.wujt.并发编程.juc_lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock: 可重入锁
 * <p>
 * fields:
 * <p>
 * Synchronizer providing all implementation mechanics
 * private final Sync sync:队列同步器，具体的锁实现方式
 * <p>
 * API:
 * public ReentrantLock(boolean fair) : 锁构造器： 公平锁和非公平锁
 * public void lock() ：获取锁资源 ： 成功则直接执行；失败进入队列；被LockSupport.part()阻塞当前线程
 *
 *  public void unlock() ：释放锁，利用   LockSupport.unpark(s.thread);唤醒所有的线程
 * <p>
 * 实现原理：
 * class Sync extends AbstractQueuedSynchronizer
 * field:
 * private transient volatile Node head : 队列同步器头
 * <p>
 * private transient volatile Node tail ： 队列同步器尾
 * <p>
 * The synchronization state.
 * private volatile int state ： 队列同步器状态
 * state为0时表示该锁没有被占，大于0时候代表该锁被一个(可重入锁)或多个线程占领（共享锁）
 *
 *private transient Thread exclusiveOwnerThread;
 *
 *
 * @author wujt
 */
public class ReentrantLockDemo {
    private static Integer counter = 0;

    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();
        try {
            reentrantLock.lock();
            System.out.println("op done!");
            counter++;
        } finally {
            reentrantLock.unlock();
            Boolean acquire = false;
            try {
                acquire = reentrantLock.tryLock(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (acquire)
                    reentrantLock.unlock();
            }
        }


    }
}
