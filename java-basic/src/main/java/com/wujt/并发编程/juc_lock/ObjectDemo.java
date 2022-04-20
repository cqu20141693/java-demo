package com.wujt.并发编程.juc_lock;

import com.wujt.并发编程.util.ConcurrentUtils;

/**
 * 对象锁： java中每一个对象都具有一把锁，获取方式为Synchronized
 * <p>
 * 实现原理：
 * 对象锁存储在对象头中 mark word中；当线程获取锁的时候通过CAS将mark word 进行修改；并将数据存储到线程中；
 * 偏向锁，轻量级锁，重量级锁：
 * 当锁不存在竞争时；利用偏向锁中的线程ID实现线程的可重入；状态为01 ：
 * 偏向锁不会自动释放；只有当存在另外的线程获取锁时才会准备释放；如果已经有线程获取；
 * 首先看之前的线程是否存在，如果存在升级。不存在释放偏向锁，继续获取偏向锁
 * <p>
 * 当锁可能存在竞争时（即当一个偏向锁存在两个线程获取时）；此时会将锁升级到轻量级锁；
 * 轻量级锁是当一定存在竞争时：出现一个线程持有锁，另一个线程去获取失败时，锁此时升级为重量级锁；
 *
 * 锁优化技术：
 * 锁粗化：
 * 锁消除：
 * 自适应自旋：
 *
 * @author wujt
 */
public class ObjectDemo {

    static final Object lock = new Object();
    static Integer time = 1000;

    public static void main(String[] args) {
        System.out.println("main线程说：试卷已经准备好，请各位子线程答题");


        for (int i = 1; i < 11; i++) {
            Thread thread = new Thread(() -> {
                System.out.println(String.format("%s 线程说： 好的，我开始答题了。。。我已经答完了", Thread.currentThread().getName()));
            }, "student" + i);
            thread.start();
        }

        Thread notify = new Thread(() -> {
            ConcurrentUtils.sleep(1);
            synchronized (lock) {
                ConcurrentUtils.sleep(time);
                lock.notify();
            }
        }, "notify");
        notify.start();
        synchronized (lock) {
            try {
                lock.wait();
                System.out.println("考试结束了:考试时长为" + time + " 纳秒");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
