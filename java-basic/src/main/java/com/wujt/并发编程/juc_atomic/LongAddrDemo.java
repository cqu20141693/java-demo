package com.wujt.并发编程.juc_atomic;

import java.util.concurrent.atomic.LongAdder;

/**
 * LongAdder的基本思路就是分散热点，将value值分散到一个数组中，不同线程会命中到数组的不同槽中，各个线程只对自己槽中的那个值进行CAS操作，
 * 这样热点就被分散了，冲突的概率就小很多。如果要获取真正的long值，只要将各个槽中的变量值累加返回。
 * <p>
 * transient volatile Cell[]cells;
 * <p>
 * //基础值
 * //1. 在没有竞争时会更新这个值；
 * //2. 在cells初始化的过程中，cells处于不可用的状态，这时候也会尝试将通过cas操作值累加到base。
 * transient volatile long base;
 * //自旋锁，通过CAS操作加锁，用于保护创建或者扩展Cell表。
 * transient volatile int cellsBusy;
 * <p>
 * LongAdder有两个值用于累加，一个是base，它的作用类似于AtomicInteger里面的value，在没有竞争的情况不会用到cells数组，它为null，这时使用base做累加，
 * 有了竞争后cells数组就上场了，第一次初始化长度为2，以后每次扩容都是变为原来的两倍，直到cells数组的长度大于等于当前服务器cpu的数量为止就不在扩容；
 * 每个线程会通过线程对cells[threadLocalRandomProbe%cells.length]位置的Cell对象中的value做累加，这样相当于将线程绑定到了cells中的某个cell对象上；
 * <p>
 * https://blog.csdn.net/u013126379/article/details/94885318
 *
 * @author wujt
 */
public class LongAddrDemo {
    public static void main(String[] args) {
        LongAdder longAdder = new LongAdder();
        longAdder.add(1);
        int intValue = longAdder.intValue();
    }
}
