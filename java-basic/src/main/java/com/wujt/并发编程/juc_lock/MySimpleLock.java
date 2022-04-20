package com.wujt.并发编程.juc_lock;

import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * 如何自己实现一个Lock ： 互斥锁
 * 1. 需要一个原子变量来表示锁的状态 ： 提供两个功能；一个是状态可见性，二是对锁状态的修改必须具备原子性
 * 利用Atomic相关的原子类存储状态；其一般具有原子性操作和可见性能力
 * 2. 如何实现线程的阻塞和唤醒
 * a. 使用对象的wait 和notify ;但是这个本身就需要使用Synchronized获取锁；
 * b. 使用LockSupport 中的park(),unpark(Thread t); 该方法不需要顺序性（死锁），并且不需要锁
 * 3. 如何存储阻塞的线程
 * a. 保证存储的原子性； 阻塞队列
 *
 *  扩展：
 *  1. 何如实现可重入： 当获取锁失败后；判断当前获取到锁的线程是不是自己，是即可以获取到锁
 *  2. 如果实现公平性： 在获取锁之前；先判断是否存在等待的线程；存在就不去获取锁了；
 *  3. 如何是实现自旋：
 *  4. 如何实现可中断锁获取：
 * @author wujt
 */
public class MySimpleLock {
    // 变量owner存储占用锁的线程
    private AtomicReference<Thread> owner = new AtomicReference<>();
    // 阻塞队列
    private volatile LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();

    // 争夺锁方法
    public void lock() {
        while (!tryLock()) {  // ①：这里为什么要用while?而不是用if呢？
            // 无法获取到锁，添加到阻塞等待队列
            waiters.add(Thread.currentThread());
            LockSupport.park();
        }
        // 如果拿到锁了，就将其移除阻塞等待队列
        // waiters.remove(Thread.currentThread());
    }

    // 尝试获取锁的方法
    public boolean tryLock() {
        // CAS机制拿当前线程跟主内存中的值对比，owner是否为null，如果是就将其值设置为当前线程
        return owner.compareAndSet(null, Thread.currentThread());
    }

    // 释放锁
    public void unlock() {
        // CAS机制拿当前线程跟主内存中的值对比，是否是同一个线程，如果是就将其值设置为null
        if (owner.compareAndSet(Thread.currentThread(), null)) {
            waiters.remove(Thread.currentThread());
            // 将所有阻塞等待队列的线程都唤醒，让他们去抢锁。（非公平）
            Optional.ofNullable(waiters).ifPresent(set -> set.forEach(LockSupport::unpark));
        }
    }
}


