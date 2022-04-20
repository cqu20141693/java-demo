package com.wujt.并发编程.share_lock;

import com.wujt.并发编程.util.ConcurrentUtils;

import java.util.concurrent.Semaphore;

/**
 * Semaphore: 可重用共享锁；
 * <p>
 * API：
 * public void acquire() ： 获取一个许可证，不存在时阻塞到同步队列中。
 * public boolean tryAcquire() ：
 * public boolean tryAcquire(long timeout, TimeUnit unit)
 * public void acquire(int permits)
 * public int drainPermits()
 * public void release(int permits)
 *
 * 实现原理：
 * 当使用Semaphore 对象 semaphore 进行acquire() 获取执行权时。如果失败；就会将自己加入到等待队列中；
 * 等到semaphore 对象调用release对象时，会唤醒头结点对应的下一个结点；会进入自旋中进行再次尝试获取锁；如果获取成功，则尝试看不是要唤醒下个节点；
 * 当前节点对应的线程则推出循环执行业务代码。（这里需要注意进入同步队列后，就必须按照队里的顺序进行逐个唤醒）
 *
 * 特点：
 * 具有公平锁和非公平锁实现
 * 可中断获取许可
 * 许可证可以释放归还。重新使用
 * <p>
 * <p>
 * 使用场景： 进行并发控制；最多处理多少个请求；否则就进行阻塞或者放弃
 *
 * @author wujt
 */
public class SemaphoreDemo {
    static int permit = 10;
    static Semaphore semaphore = new Semaphore(permit);

    public static void main(String[] args) {
        /**
         * 限流： 如果无法得到权限；就进行阻塞
         */
        testLimit();

        ConcurrentUtils.sleep(5000);

        System.out.println();
        System.out.println();
        System.out.println();

        /**
         *  通过tryAcquire 尝试获取；如果获取成功，就工作；获取失败就放过。
         */
        testLimit2();



    }

    private static void testLimit2() {
        for (int i = 1; i < 20; i++) {
            Thread limitThread = new Thread(() -> {

                    System.out.println(String.format("%s 线程说：我尝试获取许可证，否则我就不工作了。。。。。", Thread.currentThread().getName()));
                boolean acquire = semaphore.tryAcquire();
                if(acquire){
                    try {
                        System.out.println(String.format("%s 线程说：我得到许可证了，我要操作了。。。。。", Thread.currentThread().getName()));
                    }finally {
                        ConcurrentUtils.sleep(2000);
                        semaphore.release();
                        System.out.println(String.format("%s 线程说：我操作完了，我要归还许可证", Thread.currentThread().getName()));
                    }

                }else {
                    System.out.println(String.format("%s 线程说：我没有许可证，我回家了。。。。。", Thread.currentThread().getName()));
                }
            }, "thread" + i);

            limitThread.start();
        }
    }

    private static void testLimit() {
        for (int i = 1; i < 20; i++) {
            Thread limitThread = new Thread(() -> {
                try {
                    System.out.println(String.format("%s 线程说：我需要许可证，否则我就等待。。。。。", Thread.currentThread().getName()));
                    semaphore.acquire();
                    System.out.println(String.format("%s 线程说：我得到许可证了，我要操作了。。。。。", Thread.currentThread().getName()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    ConcurrentUtils.sleep(2000);
                    semaphore.release();
                    System.out.println(String.format("%s 线程说：我操作完了，我要归还许可证", Thread.currentThread().getName()));
                }
            }, "thread" + i);

            limitThread.start();
        }
    }
}
