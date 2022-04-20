package com.wujt.并发编程.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author wujt
 */
public class ThreadLocalRandomDemo {
    public static void main(String[] args) {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
    }
}
