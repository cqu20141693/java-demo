package com.gow.pulsar.test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/2
 */
@Component("common")
@Slf4j
@ConditionalOnProperty(prefix = "gow.test.pulsar", name = "common", havingValue = "true")
public class PulsarTest {
    private final ExecutorService executorService =
            new ThreadPoolExecutor(1, 1, Integer.MAX_VALUE, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));


    private void listen() {
    }

}
