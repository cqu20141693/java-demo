package com.wujt.kafka;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.wujt.concurrent.ExecutorConfig;
import com.wujt.concurrent.ProcessExecutorGroup;
import com.wujt.kafka.config.KafkaProperties;
import com.wujt.kafka.producer.KafkaProducerClient;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * @author wujt
 */
public class KafkaProducerClientTest {

    @Test
    public void stressTest() throws InterruptedException {

        int clientSize = 60;
        ExecutorConfig executorConfig = new ExecutorConfig();
        executorConfig.setProcessExecutorCount(16);
        executorConfig.setThreadPoolSize(16);
        ProcessExecutorGroup processExecutorGroup = new ProcessExecutorGroup(executorConfig);
        CountDownLatch downLatch = new CountDownLatch(clientSize);
        String topic = "origin-data-topic";
//        KafkaProperties.KAFKA_SERVER_ADDR = "10.200.58.27:9092,10.200.47.107:9092,10.200.8.87:9092";
        KafkaProperties.KAFKA_SERVER_ADDR = "localhost:9092";
        KafkaProducerClient producerClient = new KafkaProducerClient(topic, "stress-producer", true, null, false, 100*60*5, -1, downLatch);
        Consumer<ProducerRecord<Integer, String>> consumer = (record) -> {
            try {
                // 每个producer 100/s tps
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CompletableFuture.supplyAsync(() -> {
                try {
                    return producerClient.get().send(record).get(1500, TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                    throw new CompletionException(e);
                }
            }, processExecutorGroup.getExecutor(String.valueOf(record.key())));
        };
        producerClient.setConsumer(consumer);

        ThreadFactory createThreadFactory = new ThreadFactoryBuilder().setNameFormat("create-%d").setDaemon(true).build();
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(clientSize,clientSize,
                10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(executorConfig.getThreadPoolQueueSize()), createThreadFactory);
        IntStream.range(0, clientSize).forEach(i -> poolExecutor.submit(producerClient));
        downLatch.await();
        processExecutorGroup.shutdown();
    }
}
