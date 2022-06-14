package com.cc.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * 请求id提供器
 * wcc 2022/5/24
 */
public class RequestIdSupplier {

    private final AtomicInteger requestIdInc = new AtomicInteger();

    public int nextId(Predicate<Integer> doNext) {
        int requestId;
        do {
            requestId = requestIdInc.incrementAndGet();
            if (requestId <= 0) {
                requestIdInc.set(requestId = 1);
            }
        } while (doNext.test(requestId));
        return requestId;
    }

}
