package com.wujt.并发编程.juc_atomic;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 解决CAS ABA 问题；即在比较的过程中；出现内存值原来是A，后来变成了B，然后又变成了A，那么CAS进行检查时会发现值没有发生变化。
 * ABA问题的解决思路就是在变量前面添加版本号，ES利用的就是版本号实现的并发更新问题，数据库MVCC
 * （即对需要使用CAS的变量增加一个版本号变量；如果版本）
 *
 * @author wujt
 */
public class AtomicStampedReferenceDemo {
    static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(1, 1);

    public static void main(String[] args) {
        // 初始化需要共享的变量，同时初始化共享版本号

        System.out.println(String.format("reference=%s,stamp=%s", atomicStampedReference.getReference(), atomicStampedReference.getStamp()));
        // 重置stamp
        boolean attemptStamp = atomicStampedReference.attemptStamp(1, 2);
        Integer reference = atomicStampedReference.getReference();
        int stamp = atomicStampedReference.getStamp();
        if (attemptStamp) {
            System.out.println(String.format("reference=%s,stamp=%s", reference, stamp));
        }
        boolean compareAndSet = atomicStampedReference.compareAndSet(1, 2, 1, 1 + 1);
        if (!compareAndSet) {
            System.out.println("compareAndSet failed");
        }

        // 自实现AtomicInteger
        reference = getAndIncrment(atomicStampedReference, 2);
        Integer newReference = atomicStampedReference.getReference();
        if (reference.equals(newReference)) {
            System.out.println(String.format("newReference=%s,stamp=%s", reference, atomicStampedReference.getStamp()));
        }
    }

    private static Integer getAndIncrment(AtomicStampedReference<Integer> atomicStampedReference, int delta) {
        Integer reference;
        int stamp;
        boolean compareAndSet;
        do {
            reference = atomicStampedReference.getReference();
            stamp = atomicStampedReference.getStamp();
            compareAndSet = atomicStampedReference.compareAndSet(reference, reference + delta, stamp, stamp + 1);
        } while (!compareAndSet);
        return reference + 2;
    }
}
