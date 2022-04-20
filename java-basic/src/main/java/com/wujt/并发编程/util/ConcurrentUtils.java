package com.wujt.并发编程.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author wujt
 */
public class ConcurrentUtils {
    /**
     * 停止一个线程池
     *
     * @param executor
     */
    public static void stop(ExecutorService executor) {
        try {
            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("termination interrupted");
        } finally {
            if (!executor.isTerminated()) {
                System.err.println("killing non-finished tasks");
            }
            executor.shutdownNow();
        }
    }

    /**
     * 当前线程休眠
     *
     * @param nanoSeconds
     */
    public static void sleep(int nanoSeconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(nanoSeconds);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
