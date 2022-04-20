package com.wujt.server.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.wujt.server.executor.config.ExecutorConfig;
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

    private Executor[] connExecutor;

    private Executor[] extendExecutor;
    //线程池配置
    private ExecutorConfig executorConfig;

    public ProcessExecutorGroup(ExecutorConfig executorConfig) {
        this.executorConfig = executorConfig;

        ThreadFactory processThreadFactory = new ThreadFactoryBuilder().setNameFormat("process-%d").setDaemon(true).build();
        processService = new ThreadPoolExecutor(executorConfig.getThreadPoolSize(), executorConfig.getThreadPoolSize(),
                0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(executorConfig.getThreadPoolQueueSize()),
                processThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        // process executor
        int processExecutorCount = executorConfig.getProcessExecutorCount();
        processExecutor = new Executor[processExecutorCount];
        for (int i = 0; i < processExecutorCount; i++) {
            processExecutor[i] = new SequenceExecutor(processService, executorConfig.getProcessExecutorQueueSize());
        }
        // conn executor
        int connExecutorCount = executorConfig.getConnExecutorCount();
        connExecutor = new Executor[connExecutorCount];
        for (int i = 0; i < connExecutorCount; i++) {
            connExecutor[i] = new SequenceExecutor(processService, executorConfig.getConnExecutorQueueSize());
        }

        int extendExecutorCount = executorConfig.getExtendExecutorCount();
        extendExecutor = new Executor[extendExecutorCount];
        for (int i = 0; i < extendExecutorCount; i++) {
            extendExecutor[i] = new SequenceExecutor(processService, executorConfig.getExtendExecutorQueueSize());
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

    public boolean submitConnTask(String key, Runnable task) {
        int index = Math.abs(key.hashCode()) % executorConfig.getConnExecutorCount();
        try {
            connExecutor[index].execute(task);
            return true;
        } catch (Exception e) {
            log.info("the conn task execute fail , queue may full!");
            return false;
        }
    }

    public Executor getExecutorFromExtend(String key) {
        int index = Math.abs(key.hashCode()) % executorConfig.getExtendExecutorCount();
        return extendExecutor[index];
    }

    @PreDestroy
    public void shutdown() {
        processService.shutdown();
    }
}
