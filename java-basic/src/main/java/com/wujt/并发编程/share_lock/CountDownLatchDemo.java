package com.wujt.并发编程.share_lock;

import com.wujt.并发编程.util.ConcurrentUtils;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch: 共享锁，只能使用一次
 * <p>
 * API:
 * public void await() throws InterruptedException
 * public boolean await(long timeout, TimeUnit unit)
 * public void countDown()
 * <p>
 * 实现原理：
 * 利用AQS同步器状态字段作为共享数；只能初始化设置permit；
 * 使用CountDownLatch 对象countDownLatch 调用了await 方法的线程需要等待等待其他线程调用countDownLatch countDown permit 次；
 * 最后一个调用countDown的线程负责唤醒同步队列中的头结点；然后头节点利用setHeadAndPropagate 进行传播唤醒同步队列中的所有线程。
 *
 * @author wujt
 */
public class CountDownLatchDemo {
    static Integer permit = 5;
    static CountDownLatch countDownLatch = new CountDownLatch(permit);

    public static void main(String[] args) {

        System.out.println("开始工作");
        for (int i = 1; i <= permit; i++) {
            Thread thread = new Thread(() -> {
                System.out.println(String.format("%s 线程说： 我开始工作了.... 工作完了", Thread.currentThread().getName()));
                ConcurrentUtils.sleep(1000);
                countDownLatch.countDown();
            }, "Thread" + i);
            thread.start();
        }
        try {
            Thread boss = new Thread(() -> {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(String.format("%s 线程说：我协助进行汇总。。。。", Thread.currentThread().getName()));
            }, "boss");
            boss.start();
            countDownLatch.await();
            System.out.println(String.format("%s 线程说： 好的，我开始汇总。。。。", Thread.currentThread().getName()));
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

