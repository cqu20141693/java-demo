package com.wujt.并发编程.juc_atomic;

import com.google.common.util.concurrent.AtomicDouble;

import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * @author wujt
 */
public class AtomicDoubleDemo {
    public static void main(String[] args) {
        AtomicDouble atomicDouble = new AtomicDouble();
        AtomicReferenceArray<String> atomicReferenceArray = new AtomicReferenceArray<>(10);
    }
}
