package com.wujt.并发编程.juc_atomic;

import com.wujt.并发编程.util.ConcurrentUtils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * AtomicReferenceFieldUpdater： 原子类引用更新器，让普通类型的字段也能享受到原子操作
 * <p>
 * API:
 * static <U,W> AtomicReferenceFieldUpdater<U,W> newUpdater(Class<U> tclass,Class<W> vclass,String fieldName)
 * <p>
 * <p>
 * AtomicIntegerFieldUpdater： 原子类int更新器
 * <p>
 * <p>
 * API:
 * static <U> AtomicIntegerFieldUpdater<U> newUpdater(Class<U> tclass,String fieldName)
 * <p>
 * AtomicLongFieldUpdater： 原子类long 更新器
 * static <U> AtomicLongFieldUpdater<U> newUpdater(Class<U> tclass,String fieldName)
 * <p>
 * API:
 * 具有原型更新和修改的能力；并且对于int,long；具有自增和递减的操作。
 * <p>
 * 实现原理：
 *
 *
 * <p>
 * 使用场景：
 * ，假如原本有一个变量是int型，并且很多地方都应用了这个变量，但是在某个场景下，想让int型变成AtomicInteger，
 * 但是如果直接改类型，就要改其他地方的应用。AtomicIntegerFieldUpdater就是为了解决这样的问题产生的， 另外两个API类似
 * <p>
 * 使用条件：
 * 限制1：操作的目标不能是static类型
 * 限制2：操作的目标不能是final类型的，因为final根本没法修改。
 * 限制3：必须是volatile类型的数据，也就是数据本身是读一致的。
 * 限制4：属性必须对当前的Updater所在的区域是可见的，也就是private如果不是当前类肯定是不可见的，protected如果不存在父子关系也是不可见的，default如果不是在同一个package下也是不可见的。
 *
 * @author wujt
 */
public class AtomicReferenceFieldUpdaterDemo {
    public final static AtomicIntegerFieldUpdater<AtomicUpdaterInfo> intUpdater = AtomicIntegerFieldUpdater.newUpdater(AtomicUpdaterInfo.class, "age");
    private final static AtomicLongFieldUpdater<AtomicUpdaterInfo> longUpdater = AtomicLongFieldUpdater.newUpdater(AtomicUpdaterInfo.class, "time");
    private final static AtomicReferenceFieldUpdater<AtomicUpdaterInfo, String> referenceUpdater = AtomicReferenceFieldUpdater.newUpdater(AtomicUpdaterInfo.class, String.class, "name");
    private final static AtomicReferenceFieldUpdater<AtomicUpdaterInfo, Boolean> boolReferenceUpdater = AtomicReferenceFieldUpdater.newUpdater(AtomicUpdaterInfo.class, Boolean.class, "work");
    private static AtomicInteger integer = new AtomicInteger(24);

    public static void main(String[] args) {
        AtomicUpdaterInfo atomicUpdaterInfo = new AtomicUpdaterInfo(24, 123456789L, "c&01", false);
        Thread work = new Thread(() -> {
            while (true) {
                if (atomicUpdaterInfo.getWork()) {
                    System.out.println("info=" + atomicUpdaterInfo + " integer=" + integer.get());
                    ConcurrentUtils.sleep(2000);
                }
            }
        }, "work");

        Thread update = new Thread(() -> {
            while (true) {
                ConcurrentUtils.sleep(5000);
                intUpdater.incrementAndGet(atomicUpdaterInfo);
                integer.incrementAndGet();
                longUpdater.decrementAndGet(atomicUpdaterInfo);
                referenceUpdater.getAndUpdate(atomicUpdaterInfo, (value) -> {
                    String[] strings = value.split(" ");
                    StringBuilder builder = new StringBuilder();
                    if (strings.length == 1) {

                        for (char c : value.toCharArray()) {
                            builder.append(c).append(" ");
                        }

                    } else {
                        for (String s : strings)
                            builder.append(s);
                    }
                    return builder.toString().trim();
                });
                if (boolReferenceUpdater.get(atomicUpdaterInfo)) {
                    boolReferenceUpdater.compareAndSet(atomicUpdaterInfo, true, false);
                } else {
                    boolReferenceUpdater.compareAndSet(atomicUpdaterInfo, false, true);
                }
            }

        }, "update");
        work.start();
        update.start();

    }


}
