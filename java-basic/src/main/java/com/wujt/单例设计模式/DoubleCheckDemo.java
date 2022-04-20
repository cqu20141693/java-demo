package com.wujt.单例设计模式;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author wujt
 */
public class DoubleCheckDemo {

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
            // 并发量大情况下，可能初始化并没有完成。
            if (instance == null) {
                synchronized (IdGenertor.class) { // 此处为类锁
                    if (instance == null) // 其他线程获取锁后看到此处已经初始化
                        instance = new IdGenertor();
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
