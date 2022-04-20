package com.wujt.并发编程.juc_atomic;

import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * @author wujt
 */
public class AtomicReferenceArrayDemo {
    public static void main(String[] args) {
        AtomicReferenceArray<Integer> atomicReferenceArray = new AtomicReferenceArray<>(10);
    }
}
