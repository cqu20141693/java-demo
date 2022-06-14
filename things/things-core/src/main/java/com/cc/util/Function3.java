package com.cc.util;

/**
 * wcc 2022/6/6
 */
@FunctionalInterface
public interface Function3<T1, T2, T3, R> {
    R apply(T1 var1, T2 var2, T3 var3);
}
