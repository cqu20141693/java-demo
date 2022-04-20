package com.wujt.并发编程.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * sun.misc.Unsafe可以直接操控内存，被JDK广泛用于自己的包中，如java.nio和java.util.concurrent。
 * 不建议在生产环境中使用这个类。因为这个API十分不安全、不轻便、而且不稳定。
 * <p>
 * API:
 * 1、通过Unsafe类可以分配内存，可以释放内存；
 * public native long allocateMemory(long l);
 * public native long reallocateMemory(long l, long l1);
 * public native void freeMemory(long l);
 * <p>
 * 2、可以定位对象某字段的内存位置，也可以修改对象的字段值，
 * public native long staticFieldOffset(Field var1);
 * public native long objectFieldOffset(Field var1);
 * <p>
 * 3. volatile 语义操作，获取对象的field 时直接去主内存中取
 * public native Object getObjectVolatile(Object var1, long var2);
 * public native int getIntVolatile(Object var1, long var2);
 * public native boolean getBooleanVolatile(Object var1, long var2);
 * <p>
 * 4. CAS 操作
 * public final native boolean compareAndSwapInt(Object var1, long var2, int var4, int var5);
 * ....
 * <p>
 * 5. 线程挂起与恢复（LockSupport 使用）
 * public native void unpark(Object var1);
 * public native void park(boolean var1, long var2);
 *
 * @author wujt
 */
public class UnsafeDemo {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        AtomicInteger atomicInteger = new AtomicInteger();

    }
}
