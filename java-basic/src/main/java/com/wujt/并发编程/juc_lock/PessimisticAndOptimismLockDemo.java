package com.wujt.并发编程.juc_lock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wujt
 */
public class PessimisticAndOptimismLockDemo {
    public static void main(String[] args) {
        synchronized (PessimisticAndOptimismLockDemo.class) {
            System.out.println("悲观锁同步代码块");
        }
        ReentrantLock lock = new ReentrantLock();
        try {
            lock.lock();
            System.out.println("悲观锁同步代码块");
        } finally {
            lock.unlock();
        }

        AtomicInteger atomicInteger = new AtomicInteger();
        System.out.println("乐观锁代码=" + atomicInteger.getAndIncrement());
        int incrementAndGet = atomicInteger.incrementAndGet();

    }
}
