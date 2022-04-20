package com.gow.task.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author gow
 * @date 2021/7/4 0004
 */
@EnableAsync
@Configuration
@ConfigurationProperties("gow.task.async")
public class ExecutorConfig implements AsyncConfigurer {
    /**
     * 负责数据校验和存储的最大线程数
     */
    private int maxWorkerThreadCount = 20;
    /**
     * 工作线程池的任务队列的大小
     */
    private int workerTaskQueueSize = 3000;

    /**
     * Gets max worker thread count.
     *
     * @return the max worker thread count
     */
    public int getMaxWorkerThreadCount() {
        return maxWorkerThreadCount;
    }

    /**
     * Sets max worker thread count.
     *
     * @param maxWorkerThreadCount the max worker thread count
     */
    public void setMaxWorkerThreadCount(int maxWorkerThreadCount) {
        this.maxWorkerThreadCount = maxWorkerThreadCount;
    }

    /**
     * Gets worker task queue size.
     *
     * @return the worker task queue size
     */
    public int getWorkerTaskQueueSize() {
        return workerTaskQueueSize;
    }

    /**
     * Sets worker task queue size.
     *
     * @param workerTaskQueueSize the worker task queue size
     */
    public void setWorkerTaskQueueSize(int workerTaskQueueSize) {
        this.workerTaskQueueSize = workerTaskQueueSize;
    }

    /**
     * business Task executor executor.
     *
     * @return the executor
     */
    @Bean("taskExecutor")
    public TaskExecutor taskExecutor() {
        return createThreadPool(2, maxWorkerThreadCount, workerTaskQueueSize, "business-task-");
    }

    /**
     * other Task executor executor.
     *
     * @return the executor
     */
    @Bean("otherExecutor")
    public TaskExecutor otherExecutor() {
        return createThreadPool(1, 1, workerTaskQueueSize, "other-task-");
    }

    private TaskExecutor createThreadPool(int coreSize, int maxWorkerThreadCount, int workerTaskQueueSize,
                                          String threadNamePrefix) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(coreSize);
        executor.setMaxPoolSize(maxWorkerThreadCount);
        executor.setQueueCapacity(workerTaskQueueSize);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return taskExecutor();
    }

    /**
     * 配置异步任务异常处理
     * can define a custom AsyncUncaughtExceptionHandler by using AsyncConfigurer
     *
     * @return
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
