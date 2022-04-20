package com.gow.pulsar.test.consumer;

import com.gow.pulsar.core.domain.PulsarProperties;
import com.gow.pulsar.test.config.PulsarTestConfig;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.PulsarClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/7
 */
@Component("failOver")
@Slf4j
@ConditionalOnProperty(prefix = "gow.test.pulsar", name = "fail-over", havingValue = "true")
public class FailOverConsumer {
    // consumer task is always 
    private final ExecutorService executorService =
            new ThreadPoolExecutor(3, 3, Integer.MAX_VALUE, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));

    @Autowired
    private PulsarClient pulsarClient;
    @Autowired
    private PulsarProperties properties;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private PulsarTestConfig pulsarTestConfig;
    // failOver
    private final String topic = "persistent://gow/persistent/p-failover-test-topic";

}
