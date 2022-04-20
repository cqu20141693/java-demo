package com.wujt.并发编程.juc_lock;

import com.wujt.并发编程.util.ConcurrentUtils;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition: 条件锁，实现线程间通信和协调 ，类似于Object的wait,notify
 * java 7 开始支持
 * API:
 * public void await()
 * Causes the current thread to wait until it is signalled or interrupted.
 * <p>
 * public boolean await(long time, TimeUnit unit)
 * Causes the current thread to wait until it is signalled or interrupted, or the specified waiting time elapses.
 * <p>
 * public long awaitNanos(long nanosTimeout)
 * Causes the current thread to wait until it is signalled or interrupted, or the specified waiting time elapses.
 * <p>
 * public long awaitUninterruptibly()
 * Causes the current thread to wait until it is signalled.
 * <p>
 * public long awaitUntil()
 * Causes the current thread to wait until it is signalled or interrupted, or the specified deadline elapses.
 * <p>
 * public void signal()
 * Wakes up one waiting thread.
 * <p>
 * public void signalAll()
 * Wakes up all waiting threads.
 *
 * @author wujt
 */
public class ConditionDemo {
    static ReentrantLock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {

        /**
         * 可重入锁中Condition使用条件是需要先获取到锁资源
         *
         * 原因： 当我们调用 await() 方法时会释放当前线程的锁资源，如果当前线程并不是锁的持有者就会抛出监视器状态异常
         * 当我们调用 single()方法时需要检查当前线程是否持有当前锁资源，没有持有就会抛出异常；
         * 只有当拥有锁权限才能对锁资源进行操作；
         *
         */
        testCondition1();

        /**
         *  当我们进行线程间通信时；
         *  利用await()将线程放入条件队列中的等待；
         *  然后通过single()将条件队列中的等待线程移动到同步队列中等待；
         *  最后通过释放锁实现对同步队列中的阻塞线程实现FIFO唤醒执行。
         */
        testSingle();
    }

    private static void testSingle() {
        Thread await = new Thread(() -> {
            lock.lock();
            try {
                System.out.println(String.format("Thread=%s hold lock", Thread.currentThread().getName()));
                ConcurrentUtils.sleep(2);
                System.out.println("I am invoking await method");
                condition.await();
            } catch (InterruptedException e) {
                System.out.format("Thread=%s was interrupted while waiting\n", Thread.currentThread().getName());
            } finally {
                lock.unlock();
            }
        }, "await");
        Thread single = new Thread(() -> {
            lock.lock();
            try {
                System.out.println(String.format("Thread=%s hold lock", Thread.currentThread().getName()));
                condition.signal();
                System.out.println("I am invoking signal method");
            } finally {
                lock.unlock();
            }

        }, "single");
        Thread singleAll = new Thread(() -> {
            lock.lock();
            try {
                System.out.println(String.format("Thread=%s hold lock", Thread.currentThread().getName()));
                condition.signalAll();
                System.out.println("I am invoking singleAll method");
            } finally {
                lock.unlock();
            }

        }, "singleAll");
        // 测试正常流程
        await.start();
        ConcurrentUtils.sleep(2);
        single.start();
        singleAll.start();
    }

    private static void testCondition1() throws InterruptedException {
        Condition condition = lock.newCondition();
        try {
            condition.await();
        } catch (IllegalMonitorStateException e) {
            System.out.println("Condition await use condition is Thread must has the Lock resource");
        }
        try {
            condition.signal();
        } catch (IllegalMonitorStateException e) {
            System.out.println("Condition single use condition is Thread must has the Lock resource");
        }

    }
}
