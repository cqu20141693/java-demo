package com.wujt.concurrent;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.*;

/**
 * @author wujt
 */
@Component
@Slf4j
public class ProcessExecutorGroup {
    /**
     * 进行后台处理的线程池
     */
    private ExecutorService processService;
    // 任务处理器
    private Executor[] processExecutor;
    //线程池配置
    private ExecutorConfig executorConfig;

    public ProcessExecutorGroup(ExecutorConfig executorConfig) {
        this.executorConfig = executorConfig;

        ThreadFactory processThreadFactory = new ThreadFactoryBuilder().setNameFormat("process-%d").setDaemon(true).build();
        processService = new ThreadPoolExecutor(executorConfig.getThreadPoolSize(), executorConfig.getThreadPoolSize(),
                0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(executorConfig.getThreadPoolQueueSize()),
                processThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        int processExecutorCount = executorConfig.getProcessExecutorCount();
        processExecutor = new Executor[processExecutorCount];
        for (int i = 0; i < processExecutorCount; i++) {
            processExecutor[i] = new SequenceExecutor(processService, executorConfig.getProcessExecutorQueueSize());
        }

    }

    public boolean submitProcessTask(String key, Runnable task) {
        int index = Math.abs(key.hashCode()) % executorConfig.getProcessExecutorCount();
        try {
            processExecutor[index].execute(task);
            return true;
        } catch (Exception e) {
            log.info("the process task execute fail , queue may full!");
            return false;
        }
    }

    public Executor getExecutor(String key) {
        int index = Math.abs(key.hashCode()) % executorConfig.getProcessExecutorCount();
        return processExecutor[index];
    }

    @PreDestroy
    public void shutdown() {
        processService.shutdown();
    }
}
