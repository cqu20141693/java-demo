package com.wujt.client;

import lombok.Data;
import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 任务
 * wcc 2022/8/16
 */
@Data
public class Task {

    static final ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor();
    static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public static void scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        scheduledExecutorService.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    public static void schedule(Runnable task, long initialDelay, TimeUnit unit) {

            scheduledExecutorService.schedule(task, initialDelay, unit);

    }
}
