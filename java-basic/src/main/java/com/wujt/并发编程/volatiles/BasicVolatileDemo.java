package com.wujt.并发编程.volatiles;

import com.wujt.并发编程.thread.MyThead;
import com.wujt.并发编程.util.ConcurrentUtils;

/**
 * volatile 修饰基本类型： 线程间的可见性；和指令重排序
 *
 * @author wujt
 */
public class BasicVolatileDemo {
    //类变量
    final static int max = 10;
    static int init_value = 0;
    static volatile int value = 0;

    public static void main(String[] args) {
        // notUseVolatile();
        System.out.println("user:");
        // test
        useVolatile();
    }


    private static void notUseVolatile() {
        Runnable reader = () -> {
            int localValue = init_value;
            while (localValue < max) {
                if (init_value != localValue) {
                    System.out.printf("The init_value is update ot [%d]\n", init_value);
                    //对localValue进行重新赋值
                    localValue = init_value;
                }
            }
        };
        //启动reader
        new MyThead(reader, "Reader").start();

        Runnable update = () -> {
            int localValue = init_value;
            while (localValue < max) {
                //修改init_value
                System.out.printf("The init_value will be changed to [%d]\n", ++localValue);
                init_value = localValue + 1;
                ConcurrentUtils.sleep(2);
            }
        };

        // 启动updater
        new MyThead(update, "Updater").start();
    }

    private static void useVolatile() {
        Runnable reader = () -> {
            int localValue = value;
            while (localValue < max) {
                if (value != localValue) {
                    System.out.printf("The init_value is update ot [%d]\n", value);
                    //对localValue进行重新赋值
                    localValue = value;
                }
            }
        };
        //启动reader
        new MyThead(reader, "Reader").start();

        Runnable update = () -> {
            int localValue = value;
            while (localValue < max) {
                // z这里先读；后修改；如果我们将读的数据修改为localValue；会存在指令重排序问题
                System.out.printf("The init_value will be changed to [%d]\n", ++value);
                value = ++localValue;
                // 防止出现更新线程获取执行片段直接更新完成；而读取线程并没并发读取
                ConcurrentUtils.sleep(2);
            }
        };

        // 启动updater
        new MyThead(update, "Updater").start();
    }
}
