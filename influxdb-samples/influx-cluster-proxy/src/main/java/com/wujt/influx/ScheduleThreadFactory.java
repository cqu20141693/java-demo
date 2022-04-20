package com.wujt.influx;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zc
 * @date 2019/7/12 10:09
 */
public class ScheduleThreadFactory implements ThreadFactory {
    private final AtomicInteger threadNum;

    private final ThreadGroup threadGroup;

    public ScheduleThreadFactory() {
        threadNum = new AtomicInteger(0);
        SecurityManager s = System.getSecurityManager();
        threadGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {
        String threadName = "influx-schedule-thread-" + threadNum.incrementAndGet();
        return new Thread(threadGroup, r, threadName);
    }
}
