package com.gow.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wujt  2021/5/14
 */
@Configuration
public class ExecutorConfiguration {

    @Bean("redisMQExecutor")
    public Executor redisMQExecutor() {
        return createThreadPool(1, 1, 2048);
    }

    /**
     * @param coreSize
     * @param maxWorkerThreadCount
     * @param workerTaskQueueSize
     * @return org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
     * @date 2021/5/14 10:03
     */
    private ThreadPoolTaskExecutor createThreadPool(int coreSize, Integer maxWorkerThreadCount, Integer workerTaskQueueSize) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(coreSize);
        executor.setMaxPoolSize(maxWorkerThreadCount);
        executor.setQueueCapacity(workerTaskQueueSize);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("worker-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }
}
