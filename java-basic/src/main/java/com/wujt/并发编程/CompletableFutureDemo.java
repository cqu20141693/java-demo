package com.wujt.并发编程;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * CompletionStage 接口的作用是讲多个任务进行串行执行，并行执行，聚合执行（and聚合和or聚合）和异常处理
 * <p>
 * 串、并行关系
 * then 直译【然后】，也就是表示下一步，所以通常是一种串行关系体现, then 后面的单词（比如 run /apply/accept）
 * 无返回值串行： Runnable
 * CompletableFuture<Void> thenRun(Runnable action)
 * 无返回值并行： Runnable
 * CompletableFuture<Void> thenRunAsync(Runnable action)
 * CompletableFuture<Void> thenRunAsync(Runnable action, Executor executor)
 * 有返回值串行：Function
 * <U> CompletableFuture<U> thenApply(Function<? super T,? extends U> fn)、
 * 有返回值并行：Function
 * <U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn)
 * <U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn, Executor executor)
 * 无返回值串行：Consumer
 * CompletableFuture<Void> thenAccept(Consumer<? super T> action)
 * 无返回值并行：Consumer
 * CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> action)
 * CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> action, Executor executor)
 * 有返回值同步： Function, Function返回之为CompletionStage<U>
 * <U> CompletableFuture<U> thenCompose(Function<? super T, ? extends CompletionStage<U>> fn)
 * 有返回值异步： Function, Function返回之为CompletionStage<U>
 * <U> CompletableFuture<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn)
 * <U> CompletableFuture<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn, Executor executor)
 * <p>
 * 聚合 And 关系
 * combine... with... 和 both...and... 都是要求两者都满足
 * <U,V> CompletableFuture<V> thenCombine(CompletionStage<? extends U> other, BiFunction<? super T,? super U,? extends V> fn)
 * <U,V> CompletableFuture<V> thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super T,? super U,? extends V> fn)
 * <U,V> CompletableFuture<V> thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super T,? super U,? extends V> fn, Executor executor)
 *
 * <U> CompletableFuture<Void> thenAcceptBoth(CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action)
 * <U> CompletableFuture<Void> thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action)
 * <U> CompletableFuture<Void> thenAcceptBothAsync( CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action, Executor executor)
 * <p>
 * CompletableFuture<Void> runAfterBoth(CompletionStage<?> other, Runnable action)
 * CompletableFuture<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action)
 * CompletableFuture<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action, Executor executor)
 * <p>
 * 聚合 Or 关系
 * <U> CompletableFuture<U> applyToEither(CompletionStage<? extends T> other, Function<? super T, U> fn)
 * <U> CompletableFuture<U> applyToEitherAsync(、CompletionStage<? extends T> other, Function<? super T, U> fn)
 * <U> CompletableFuture<U> applyToEitherAsync(CompletionStage<? extends T> other, Function<? super T, U> fn, Executor executor)
 * <p>
 * CompletableFuture<Void> acceptEither(CompletionStage<? extends T> other, Consumer<? super T> action)
 * CompletableFuture<Void> acceptEitherAsync(CompletionStage<? extends T> other, Consumer<? super T> action)
 * CompletableFuture<Void> acceptEitherAsync(CompletionStage<? extends T> other, Consumer<? super T> action, Executor executor)
 * <p>
 * CompletableFuture<Void> runAfterEither(CompletionStage<?> other, Runnable action)
 * CompletableFuture<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action)
 * CompletableFuture<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action, Executor executor)
 * <p>
 * 异常处理
 * exceptionally 就相当于 catch，出现异常，将会跳过 thenApply 的后续操作，直接捕获异常，进行一场处理
 * CompletableFuture<T> exceptionally(Function<Throwable, ? extends T> fn)
 * CompletableFuture<T> exceptionallyAsync(Function<Throwable, ? extends T> fn)
 * CompletableFuture<T> exceptionallyAsync(Function<Throwable, ? extends T> fn, Executor executor)
 * <p>
 * CompletableFuture<T> whenComplete(BiConsumer<? super T, ? super Throwable> action)
 * CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action)
 * CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action, Executor executor)
 * 用多线程，良好的习惯是使用 try/finally 范式，handle 就可以起到 finally 的作用， handle 接受两个参数，一个是正常返回值，一个是异常
 * <U> CompletableFuture<U> handle(BiFunction<? super T, Throwable, ? extends U> fn)
 * <U> CompletableFuture<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn)
 * <U> CompletableFuture<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn, Executor executor)
 * <p>
 * 获取Future结果
 * public T join() ： 无异常获取结果
 * public T get() throws InterruptedException, ExecutionException : 中断异常和Executor异常
 * <p>
 * 支持延迟和超时处理
 *
 * @author wujt
 */
@Slf4j
public class CompletableFutureDemo {
    public static void main(String[] args) {

        /**
         * 没有指定Executor的方法会使用ForkJoinPool.commonPool() 作为它的线程池执行异步代码。如果指定线程池，则使用指定的线程池运行。以下所有的方法都类同。
         *
         * runAsync方法不支持返回值。
         * supplyAsync可以支持返回值。
         *
         */

        CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("database ops  ...");
            return System.currentTimeMillis();
        });

        long time = 0;
        try {
            time = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("time = " + time);


        exceptionTest();
        handleTest();
        timeoutTest();

    }

    private static void timeoutTest() {

        CompletableFuture<String> timeout = CompletableFuture.completedFuture("hello world")
                .orTimeout(1, TimeUnit.SECONDS)
                .thenApply((value) -> value.toUpperCase());
        log.info("timeout result={}", timeout.join());
        CompletableFuture<String> objectCompletableFuture = new CompletableFuture<>();
        CompletableFuture<String> gow = objectCompletableFuture.completeOnTimeout("gow", 1, TimeUnit.SECONDS);
        log.info("completeOnTimeout result={}", gow.join());

    }

    private static void exceptionTest() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException();
        })
                .exceptionally(ex -> "errorResultA")
                .thenApply(resultA -> resultA + " resultB")
                .thenApply(resultB -> resultB + " resultC")
                .thenApply(resultC -> resultC + " resultD");

        System.out.println(future.join());
    }

    private static void handleTest() {
        Integer age = -1;

        CompletableFuture<String> maturityFuture = CompletableFuture.supplyAsync(() -> {
            if (age < 0) {
                throw new IllegalArgumentException("何方神圣？");
            }
            if (age > 18) {
                return "大家都是成年人";
            } else {
                return "未成年禁止入内";
            }
        }).thenApply((str) -> {
            log.info("游戏开始");
            return str;
        }).handle((res, ex) -> {
            if (ex != null) {
                log.info("必有蹊跷，来者" + ex.getMessage());
                return "Unknown!";
            }
            return res;
        });

        log.info(maturityFuture.join());
    }

}
