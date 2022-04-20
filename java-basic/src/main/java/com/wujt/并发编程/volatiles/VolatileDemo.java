package com.wujt.并发编程.volatiles;

import java.util.concurrent.atomic.AtomicLong;

/**
 * volatile ：轻量级的线程同步：保证变量的线程间可见性
 * 修饰对象时；对象中的field 也具有validate 语义；可以被其他线程立即可见
 * <p>
 * 语义：
 * 线程间可见性
 * 禁止指令重排序 ：
 * <p>
 * 实现原理：
 * 内存屏障技术实现，基于处理器的Lock指令的，这个指令会使得对变量的修改立马刷新回主内存，同时使得其他CPU中这个变量的副本失效
 * <p>
 * 应用场景：
 * 乐观锁实现：利用volatile 可见性语义和cas原子性操作修改状态强占锁
 * 多线程中指令重排序导致程序不正确： 单例模式double check。
 * <p>
 * 特点：
 * 无法保证操作的原子性；只能是变量的可见性和指令重排序问题。
 *
 * @author wujt
 */
public class VolatileDemo {

    static class IdGenertor {
        private AtomicLong counter = new AtomicLong();
        // 在低版本的java中 new对象和对象的初始化并不是一个原子操作：分配内存空间，初始化对象，将对象指向刚分配的内存空间 :
        // 但是这里可能被指令重排序，导致先将对象进行分配；导致其他线程在第一次检查处！=null,得到了没有初始化的对象
        // 高版本的java已经解决；可以不用volatile关键字禁止指令从排序
        private static volatile IdGenertor instance;

        // 1. 保证构造函数为私有化
        private IdGenertor() {
        }

        public static IdGenertor getDoubleCheckInstance() {
            // 并发量大情况下
            if (instance == null) {
                synchronized (IdGenertor.class) { // 此处为类锁
                    if (instance == null) // 其他线程获取锁后看到此处已经初始化
                    {
                        instance = new IdGenertor();
                    }
                }
            }
            return instance;
        }

        public Long getId() {
            return counter.incrementAndGet();
        }
    }

    public static void main(String[] args) {
        IdGenertor instance = IdGenertor.getDoubleCheckInstance();
        System.out.println("id=" + instance.getId());
    }
}
