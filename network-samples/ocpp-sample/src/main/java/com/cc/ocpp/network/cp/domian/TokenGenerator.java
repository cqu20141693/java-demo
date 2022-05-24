package com.cc.ocpp.network.cp.domian;

import lombok.Data;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * token 生成器
 * wcc 2022/4/30
 */
@Data
public class TokenGenerator {

    private static final int range = 0x9999 - 0x1000;
    private static final AtomicInteger counter;

    static {
        Random random = new Random();
        counter = new AtomicInteger(random.nextInt(range));
    }
    public TokenGenerator() {
    }

    public static boolean validate(short token) {
        int num = token & 0xffff;
        return num >= 0x1000 && num < 0x9999;
    }

    public static short generate() {
        counter.compareAndSet(range, 0);
        return (short) ((counter.getAndIncrement() + 0x1000) & 0xffff);
    }
}
