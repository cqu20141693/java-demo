package com.wujt.并发编程.juc_lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * AbstractQueuedSynchronizer ： 同步器
 * <p>
 * field:
 * volatile int state :同步状态，利用cas 实现原子性修改；同时也是强占锁的关键
 * volatile Node head、tail : 阻塞队列
 * <p>
 * API：
 * public final void acquire(int arg)： 获取独占锁
 * public final void acquireInterruptibly(int arg)： 响应中断的获取锁
 * public final boolean release(int arg)： 释放独占锁
 * <p>
 * public final void acquireShared(int arg) ： 获取共享锁
 * public final boolean releaseShared(int arg)： 释放共享锁
 *
 * 可重入锁：检测锁对象的线程持有者和当前获取锁的线程是否一致
 * 公平锁： 检测当前锁是否存在等待线程队列
 * 互斥锁：state只能具有0，1两个状态
 * 共享锁：
 *   信号量锁的状态字段最大值为令牌数，当获取锁进行减法，直到为零，释放锁，则将令牌加回
 *   并发锁：状态字段最大值为最大线程数，每一个线程准备好，则状态减一，并线程等待，
 *   当状态为零时，触发唤醒所有等待线程，CyclicBarrier 表示可以重用，CountDownLatch只能使用过一次
 *
 *
 * 实现原理：
 * 获取锁： 首先
 * @author wujt
 */
public class AQSDemo {
    public static void main(String[] args) {

        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        lock.unlock();

        boolean b = lock.tryLock();
        if(b){
            lock.unlock();
        }
    }
}
