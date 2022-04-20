package com.wujt.java9;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author gow 2021/06/13
 */
@Slf4j
public class CompletableFutureDemo {

    public static void main(String[] args) {
        CompletableFuture<String> completableFuture = new CompletableFuture();
        completableFuture.complete("success");
        Executor defaultExecutor = completableFuture.defaultExecutor();
        // 获取一个新的CompletableFuture
        CompletableFuture<String> newIncompleteFuture = completableFuture.newIncompleteFuture();
        newIncompleteFuture.complete("new");
        CompletableFuture<String> copy = completableFuture.copy();

        // join 方法是需要获取结果的，所以会一直阻塞直到完成
        log.info("completableFuture={},new={},copy={}", completableFuture.join(), newIncompleteFuture.join(), copy.join());
    }
}
