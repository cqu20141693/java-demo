package com.wujt.并发编程.volatiles;

import com.wujt.并发编程.thread.MyThead;
import com.wujt.并发编程.util.ConcurrentUtils;

/**
 *  volatile 修饰对象时；对象中的field 也具有validate 语义；可以被其他线程立即可见
 *
 * 语义：
 * 线程间可见性
 * 禁止指令重排序 ：
 *
 * 实现原理：
 * 内存屏障技术实现，基于处理器的Lock指令的，这个指令会使得对变量的修改立马刷新回主内存，同时使得其他CPU中这个变量的副本失效
 *
 * 应用场景：
 * 乐观锁实现：利用volatile 可见性语义和cas原子性操作修改状态强占锁
 * 多线程中指令重排序导致程序不正确： 单例模式double check。
 *
 * 特点：
 * 无法保证操作的原子性；只能是变量的可见性和指令重排序问题。
 * @author wujt
 */
public class ObjectVolatileDemo {
    //使用volatile修饰共享资源
    private static volatile VolatileEntity volatileEntity = new VolatileEntity(10, 0);

    public static void main(String[] args) {
        Runnable read = () -> {
            int localValue = volatileEntity.getInit_value();
            while (localValue < volatileEntity.getMax()) {
                if (volatileEntity.getInit_value() != localValue) {
                    System.out.printf("The init_value is update ot [%d]\n", volatileEntity.getInit_value());
                    //对localValue进行重新赋值
                    localValue = volatileEntity.getInit_value();
                }
            }
        };
        new MyThead(read, "Reader").start();

        Runnable update = () -> {
            int localValue = volatileEntity.getInit_value();
            while (localValue < volatileEntity.getMax()) {
                //修改init_value
                System.out.printf("The init_value will be changed to [%d]\n", +volatileEntity.getInit_value());
                volatileEntity.setInit_value(++localValue);
                ConcurrentUtils.sleep(2);
            }
        };
        new MyThead(update, "Updater").start();
    }
}
