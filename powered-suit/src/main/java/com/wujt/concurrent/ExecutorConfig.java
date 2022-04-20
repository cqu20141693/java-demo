package com.wujt.concurrent;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wujt
 */
@Configuration
@ConfigurationProperties(prefix = "client.executor")
@Data
public class ExecutorConfig {
    private int processExecutorCount = Runtime.getRuntime().availableProcessors();

    private int processExecutorQueueSize = 51200;

    private int threadPoolSize = Runtime.getRuntime().availableProcessors();

    private int threadPoolQueueSize = 1024;
}
