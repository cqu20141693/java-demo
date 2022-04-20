package com.wujt.collections.list;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * CopyOnWriteArrayList : 线程安全且读操作无锁的ArrayList，写操作则通过创建底层数组的新副本来实现，
 * 是一种读写分离的并发策略，我们也可以称这种容器为"写时复制器"，Java并发包中类似的容器还有CopyOnWriteSet
 * <p>
 * 实现原理：
 * final transient ReentrantLock lock = new ReentrantLock() : 利用锁实现对数组的修改操作（添加,更新和删除）
 * private transient volatile Object[] array : 使用volatile 修饰数组；当数组的引用改变时，对其他读线程都是立即可见的(线程内存失效,读主存)
 * 在修改操作中；都是先copy 了一份原数组，然后修改；再将修改后的数组赋值给原数组，保证了数组级的可见性和元素级的修改。
 * <p>
 * 存在的问题：
 * 1. 内存消耗问题
 * 2. 无法保证实时性；数据最终一致性问题；
 * 3. 修改时使用了锁；并发性一般
 * {@link java.util.Observable} notifyObservers 方法中也是拷贝数据进行通知；然后保证数据的最终一致性
 * <p>
 * 特点：
 * 1. 每次都是直接copy 的一个数组，所以不存在扩容问题，但是对内存的消耗很大
 * 2. 线程安全，无锁读取
 *
 * @author wujt
 */
public class CopyOnWriteArrayListDemo {
    public static void main(String[] args) {
        CopyOnWriteArrayList<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList();
        copyOnWriteArrayList.add(1);
        copyOnWriteArrayList.forEach(value -> System.out.println(value));
        // 获取元素时没有加锁；当数据修改完后，没有进行重新数组赋值前；获取的数据都是老的数组。
        Integer value = copyOnWriteArrayList.get(0);
        System.out.println(value);
    }
}
