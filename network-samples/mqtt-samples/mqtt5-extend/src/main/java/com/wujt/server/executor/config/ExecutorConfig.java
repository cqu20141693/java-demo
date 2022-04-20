package com.wujt.server.executor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wujt
 */
@Configuration
@ConfigurationProperties(prefix = "broker.executor")
public class ExecutorConfig {
    private int connExecutorCount = 16;

    private int connExecutorQueueSize = 25600;

    private int extendExecutorCount = 16;

    private int extendExecutorQueueSize = 25600;

    private int processExecutorCount = 32;

    private int processExecutorQueueSize = 51200;

    private int threadPoolSize = 64;

    private int threadPoolQueueSize = 25600;

    public int getConnExecutorCount() {
        return connExecutorCount;
    }

    public void setConnExecutorCount(int connExecutorCount) {
        this.connExecutorCount = connExecutorCount;
    }

    public int getConnExecutorQueueSize() {
        return connExecutorQueueSize;
    }

    public void setConnExecutorQueueSize(int connExecutorQueueSize) {
        this.connExecutorQueueSize = connExecutorQueueSize;
    }

    public int getExtendExecutorCount() {
        return extendExecutorCount;
    }

    public void setExtendExecutorCount(int extendExecutorCount) {
        this.extendExecutorCount = extendExecutorCount;
    }

    public int getExtendExecutorQueueSize() {
        return extendExecutorQueueSize;
    }

    public void setExtendExecutorQueueSize(int extendExecutorQueueSize) {
        this.extendExecutorQueueSize = extendExecutorQueueSize;
    }

    public int getProcessExecutorCount() {
        return processExecutorCount;
    }

    public void setProcessExecutorCount(int processExecutorCount) {
        this.processExecutorCount = processExecutorCount;
    }

    public int getProcessExecutorQueueSize() {
        return processExecutorQueueSize;
    }

    public void setProcessExecutorQueueSize(int processExecutorQueueSize) {
        this.processExecutorQueueSize = processExecutorQueueSize;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public int getThreadPoolQueueSize() {
        return threadPoolQueueSize;
    }

    public void setThreadPoolQueueSize(int threadPoolQueueSize) {
        this.threadPoolQueueSize = threadPoolQueueSize;
    }
}
