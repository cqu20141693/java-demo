package com.wujt.并发编程.threadlocal;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 线程级全局变量
 * ThreadLocal 每个线程独立
 * InheritableThreadLocal 子线程可以继承父线程的线程级全局变量
 * <p>
 * 原理是对线程成员变量的设置和获取
 * ThreadLocal.ThreadLocalMap threadLocals = null;  ：
 * ThreadLocal  实质就是将数据存储到线程类filed中；初始化时将ThreadLocal 对象设置为Key，初始化数据设置为value；
 * 当ThreadLocal对象不再使用后；被回收，线程类Map中的key被回收；但是value仍然为强引用；导致内存泄露；所以需要使用后进行remove；
 *
 * API:
 * get:
 * 1. 获取当前线程
 * 2. 获取线程的ThreadLocalMap 属性
 * 3. 判断是否为空
 * 4. 不为空，直接获取返回数据
 * 5. 为空，则创建
 * ThreadLocal.ThreadLocalMap inheritableThreadLocals=null;
 *
 * https://zhuanlan.zhihu.com/p/402281916
 * @author wujt
 */
public class ThreadLocalDemo {


    public static void main(String[] args) {
        /**
         * 测试ThreadLocal 线程级变量；每次更新和查询都是对线程变量的操作
         * 原理：使用ThreadLocalMap Hash表存储线程中的所有ThreadLocal ；
         * 以ThreadLocal 作为Key,数据值作为value，获取时通过获取Map得到
         *
         * 缺点：对于线程级变量ThreadLocalMap 中存在对ThreadLocal的弱引用
         * 当ThreadLocal的强引用为0后；会自动的回收对象；但是ThreadLocalMap
         * 中的value 值不会被回收；所以再使用的时候需要
         */
        threadLocalTest();
        ArrayList<Integer> list = new ArrayList<>();
        Collections.sort(list);

    }


    private static void threadLocalTest() {
        ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
        System.out.println(threadLocal.get());
        ThreadLocal<Integer> integerThreadLocal = ThreadLocal.withInitial(() -> 1);
        System.out.println(integerThreadLocal.get());
        threadLocal.set(2);
        System.out.println(threadLocal.get());
        (new Thread(() -> {
            System.out.println("子线程启动");
            System.out.println("在子线程中访问integerThreadLocal:" + threadLocal.get());
        })).start();

        // 规范使用；实现对已经回收的ThreadLocal 对应的value进行回收
        threadLocal.remove();  // 直接对当前TheadLocal 变量进行清理和value 引用的清理
        threadLocal.set(10); // 设置成功后会对弱引用的ThreadLocal为空的进行清理
    }
}
