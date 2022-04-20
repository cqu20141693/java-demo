package com.wujt.并发编程.util;

import java.util.concurrent.locks.LockSupport;

/**
 * LockSupport: 一个灵活的线程协作工具，底层实现Unsafe
 * <p>
 * API:
 * park()：为了线程调度，禁用当前线程，除非许可可用。
 * parkNanos(long nanos) ：为了线程调度禁用当前线程，最多等待指定的等待时间，除非许可可用。
 * parkUntil(long deadline)：为了线程调度，在指定的时限前禁用当前线程，除非许可可用。
 * unpark(Thread thread)：如果给定线程的许可尚不可用，则使其可用。
 * <p>
 * <p>
 * 特性：
 * LockSupport遇到Thread.interrupt是会立刻返回的，但是不会抛出异常InterruptedExcept
 * unpark()函数可以先于park调用：不用担心阻塞和唤醒的时序问题(wait,notify)：
 *
 * @author wujt
 */
public class LockSupportDemo {
    public static void main(String[] args) {

        /**
         * 测试了 unpark 和park 时序的问题：
         *  正常逻辑： 先park 挂起线程，然后unpark 唤醒线程；
         *  灵活性： 当先unpark 线程时；会给线程一个许可唤醒，然后再调用park 时会直接唤醒执行。避免了时序问题
         *
         */
        testPark();
        blankLine();
        /**
         * 两次连续的unpark 操作；只会允许一次许可；uppark 只有当当前线程的许可不可用才会设置其可用；否则无效；
         */
        testTwoUnpark();
        blankLine();
        /**
         * LockSupport 阻塞线程；利用线程中断也可以让其唤醒返回，继续执行
         */
        testInterrupte();
    }

    private static void testInterrupte() {
        Thread park = new Thread(() -> {
            int sum = 0;
            for (int i = 0; i < 1000; i++) {
                sum += i;
            }
            System.out.println(String.format("%s 线程说：我可能要被挂起了，如果没有许可证", Thread.currentThread().getName()));
            LockSupport.park();
            if (Thread.interrupted()) {
                System.out.println(String.format("%s 线程说： 我竟然被人中断了，难受了，我要退出了", Thread.currentThread().getName()));
                return;
            }
            System.out.println(Thread.currentThread().getName() + " 线程说： 我得到许可证了，可以输出结果了： sum = " + sum);
        }, "park");

        park.start();
        System.out.println(String.format("%s 线程说： 我要中断%s线程了，它有问题。", Thread.currentThread().getName(), park.getName()));
        park.interrupt();
    }

    private static void blankLine() {
        ConcurrentUtils.sleep(2);
        System.out.println();
        System.out.println();
    }

    private static void testTwoUnpark() {
        Thread park = new Thread(() -> {
            int sum = 0;
            for (int i = 0; i < 1000; i++) {
                sum += i;
            }
            System.out.println(String.format("%s 线程说：我可能要被挂起了，如果没有许可证", Thread.currentThread().getName()));
            LockSupport.park();
            System.out.println(String.format("%s 线程说：我得到了许可证", Thread.currentThread().getName()));
            System.out.println(String.format("%s 线程说：我可能又要被挂起了，如果没有许可证", Thread.currentThread().getName()));
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + " 线程说： 我得到许可证了，可以输出结果了： sum = " + sum);
        }, "park");
        park.start();
        LockSupport.unpark(park);
        ConcurrentUtils.sleep(1000);
        LockSupport.unpark(park);
        System.out.println(String.format("%s 线程说：我给%s线程一个唤醒许可证", Thread.currentThread().getName(), park.getName()));
    }

    private static void testPark() {
        Thread park = new Thread(() -> {
            int sum = 0;
            for (int i = 0; i < 1000; i++) {
                sum += i;
            }
            System.out.println(String.format("%s 线程说：我可能要被挂起了，如果没有许可证", Thread.currentThread().getName()));
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + " 线程说： 我得到许可证了，可以输出结果了： sum = " + sum);
        }, "park");
        park.start();
        LockSupport.unpark(park);
        System.out.println(String.format("%s 线程说：我给%s线程一个唤醒许可证", Thread.currentThread().getName(), park.getName()));
    }
}
