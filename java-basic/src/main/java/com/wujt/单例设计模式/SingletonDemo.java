package com.wujt.单例设计模式;

/**
 * @author wujt
 */
public class SingletonDemo {
    private static SingletonDemo instance;

    /**
     * 线程安全的单例模式。但是Synchronized 关键字是一个很大的开销
     *
     * @return
     */
    public static synchronized SingletonDemo getCheckInstance() {
        if (instance == null) {
            instance = new SingletonDemo();
        }
        return instance;
    }

    /**
     * 非线程安全的获取实例：
     * 一个是new 对象在jvm 指令中存在三个原子指令： 分配内存空间，对象实例化，将引用赋值。
     * 这里存在实例化和赋值的重排序；导致存在其他线程读取到并为初始化的对象。
     * <p>
     * 第二个；可能存在多个线程进行初始化操作；多个线程并发读取到实例对象为null.都会进行实例化；如果实例化操作是一个很复杂，耗时的操作。没得必要。
     *
     * @return
     */
    public static SingletonDemo getUnCheckInstance() {
        if (instance == null) {
            instance = new SingletonDemo();
        }
        return instance;
    }
}
