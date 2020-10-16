package com.wujt.kafka;

import org.junit.After;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wujt
 */

public class KafkaConsumerClientTest {

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    @Test
    public void testStart() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        KafkaConsumerClient chanConsumer = new KafkaConsumerClient(KafkaProperties.CHAN_CONN_TOPIC, "DemoConsumer", false, latch);
        executor.submit(chanConsumer);
        KafkaConsumerClient dataConsumer = new KafkaConsumerClient(KafkaProperties.DATA_TOPOC, "DemoConsumer", false, latch);
        latch.await();
        executor.execute(dataConsumer);
    }

    @After
    public void after() {
        executor.shutdown();
    }
}