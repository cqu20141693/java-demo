package com.wujt.并发编程.juc_atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * atomic 乐观锁
 * <p>
 * 原理： 使用volatile 和CAS机制，通过CPU的cmpxchg指令
 * 1. volatile 保证value的可见性；即使用主存对象数据；
 * 2. 使用cmpxchg指令实现数据修改的原子性；
 * <p>
 * CAS 机制虽然无需加锁、安全且高效，但也存在一些缺点，概括如下：
 * <p>
 * 1. 循环检查的时间可能较长，不过可以限制循环检查的次数
 * 2. 只能对一个共享变量执行原子操作;对一个共享变量执行操作时，CAS能够保证原子操作，但是对多个共享变量操作时，CAS是无法保证操作的原子性的。
 * 3. 存在 ABA 问题（ABA 问题是指在 CAS 两次检查操作期间，目标变量的值由 A 变为 B，又变回 A，
 * 但是 CAS 看不到这中间的变换，对它来说目标变量的值并没有发生变化，一直是 A，所以 CAS 操作会继续更新目标变量的值。）
 * 4. Java从1.5开始JDK提供了AtomicReference类来保证引用对象之间的原子性，可以把多个变量放在一个对象里来进行CAS操作。
 * 5. jdk1.5提供AtomicStampedReference解决ABA问题，通过每次操作加上邮戳版本号，通过版本号和期望值共同实现乐观锁
 *
 * @author wujt
 */
public class AtomicIntegerDemo {
    public static void main(String[] args) {

        AtomicInteger atomicInteger = new AtomicInteger();
        int andIncrement = atomicInteger.getAndIncrement();
        int get = atomicInteger.get();


    }
}
