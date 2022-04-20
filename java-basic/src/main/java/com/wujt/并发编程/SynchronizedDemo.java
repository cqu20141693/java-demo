package com.wujt.并发编程;

/**
 * https://xiaomi-info.github.io/2020/03/24/synchronized/
 * https://www.cnblogs.com/aspirant/p/11470858.html
 * 简单的来说偏向锁通过对比 Mark Word thread id 解决加锁问题。
 * 而轻量级锁是通过用 CAS 操作 Mark Word 和自旋来解决加锁问题，避免线程阻塞和唤醒而影响性能。
 * 重量级锁是将除了拥有锁的线程以外的线程都阻塞。
 *
 * @author wujt
 */
public class SynchronizedDemo {
    public static void main(String[] args) {


        synchronized (SynchronizedDemo.class) {
            System.out.println("hello");
        }


    }
}
