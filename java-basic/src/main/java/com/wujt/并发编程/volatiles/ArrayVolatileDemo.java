package com.wujt.并发编程.volatiles;

import com.wujt.并发编程.thread.MyThead;
import com.wujt.并发编程.util.ConcurrentUtils;

/**
 * volatile 修饰数组时，其中的元素不会存在volatile语义；所以当元素修改时，不会对其他线程立即可见
 * Doug Lea每次在获取数组的元素时，采用Unsafe类的getObjectVolatile方法
 *
 * @author wujt
 */
public class ArrayVolatileDemo {

    public static volatile int[] ints = new int[5];

    public static int max = 10;
    private static int index = 0;

    public static void main(String[] args) {

        // 对比与简单类型和引用类型使用volatile; 对数组使用volatile时不会对元素具有volatile特性
        Runnable read = () -> {
            int localValue = ints[index];
            while (localValue < max) {
                if (localValue != ints[index]) {
                    System.out.printf("The ints[%d] is update ot [%d]\n", index, ints[index]);
                }
                localValue = ints[index];
            }
        };
        new MyThead(read, "Reader").start();
        Runnable update = () -> {
            int localValue = ints[index];
            while (localValue < max) {
                //修改init_value
                System.out.printf("The ints[%d] will be changed to [%d]\n", index, +ints[index]);
                ints[index] = ++localValue;
                ConcurrentUtils.sleep(2);
            }
        };
        new MyThead(update, "Updater").start();
    }
}
