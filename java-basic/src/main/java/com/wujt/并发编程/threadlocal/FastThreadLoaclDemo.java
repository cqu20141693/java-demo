package com.wujt.并发编程.threadlocal;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.FastThreadLocalThread;

/**
 * http://www.jiangxinlingdu.com/interview/2019/07/01/fastthreadlocal.html
 * @author wujt
 */
public class FastThreadLoaclDemo {
    public static void main(String[] args) {
        /**
         * Netty 自实现的一种线程级变量 ： FastThreadLocal
         * 原理：
         * 利用 FastThreadLocal 存储访问的下标 index
         * 利用FastThreadLocalThread 线程类存储本地变量：
         * cleanupFastThreadLocals ：
         * InternalThreadLocalMap:
         * 利用InternalThreadLocalMap 线程唯一性；
         * 其提供了一个AtomicInteger nextIndex 实现对FastThreadLocal index的设置；
         * 同时提供了一个 Object[] indexedVariables 数组存储数据；
         *
         * 优势： 避免了jdk自带类内存泄露问题；同时利用数据下标访问性能提升
         *
         */
        FastThreadLocal<Integer> fastThreadLocal = new FastThreadLocal<>();
        System.out.println(Thread.currentThread().getName() + "====" + fastThreadLocal.get());
        fastThreadLocal.set(2);
        System.out.println(Thread.currentThread().getName() + "====" + fastThreadLocal.get());
        new FastThreadLocalThread(() -> {
            System.out.println(Thread.currentThread().getName() + "====" + fastThreadLocal.get());
            fastThreadLocal.set(2);
            System.out.println(Thread.currentThread().getName() + "====" + fastThreadLocal.get());

        }, "fastThreadLocal1").start();


    }
}
