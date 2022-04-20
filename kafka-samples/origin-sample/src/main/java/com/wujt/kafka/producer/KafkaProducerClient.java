package com.wujt.kafka.producer;

import com.wujt.kafka.config.KafkaProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * https://juejin.cn/post/6844903700670971911 调优方案
 * @author wujt
 */
@Slf4j
public class KafkaProducerClient implements Runnable {
    private final KafkaProducer<Integer, String> producer;
    private final String topic;
    private final Boolean isAsync;
    private int numRecords;
    private final CountDownLatch latch;
    // 生产者一个批次等待的大小，当延迟时间内等待的消息字节数在配置内可以一个批次发送
    private Integer batchSize = 16384;
    //消息延迟发送的毫秒数，目的是为了等待多个消息，在同一批次发送，减少网络请求
    private Integer lingerMS = 1;
    // 生产者一个批次发送的最大字节数
    private Integer maxRequestSize = 1048576;
    // kafka 缓存大小默认32MB
    private Integer bufferMemory = 33554432;

    public Consumer<ProducerRecord<Integer, String>> getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer<ProducerRecord<Integer, String>> consumer) {
        this.consumer = consumer;
    }

    private Consumer<ProducerRecord<Integer, String>> consumer;

    public KafkaProducerClient(final String topic,
                               final String clientId,
                               final Boolean isAsync,
                               final String transactionalId,
                               final boolean enableIdempotence,
                               final int numRecords,
                               final int transactionTimeoutMs,
                               final CountDownLatch latch) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_SERVER_ADDR);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMS);
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, maxRequestSize);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        if (transactionTimeoutMs > 0) {
            props.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, transactionTimeoutMs);
        }
        if (transactionalId != null) {
            props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, transactionalId);
        }
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, enableIdempotence);

        producer = new KafkaProducer<>(props);
        this.topic = topic;
        this.isAsync = isAsync;
        this.numRecords = numRecords;
        this.latch = latch;
    }

    public KafkaProducer<Integer, String> get() {
        return producer;
    }

    CompletableFuture<RecordMetadata> send(ProducerRecord<Integer, String> record, ExecutorService executorService) {
        return Optional.ofNullable(executorService).map(executor -> CompletableFuture.supplyAsync(() -> {
            try {
                return producer.send(record).get(1500, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }, executor)).orElse(directSend(record));
    }

    private CompletableFuture<RecordMetadata> directSend(ProducerRecord<Integer, String> record) {
        //不进行异步监听，直接发
        producer.send(record);
        return null;
    }


    @Override
    public void run() {
        assert consumer != null;
        int messageKey = 1;
        int recordsSent = 1;
        long start = System.currentTimeMillis();
        log.info("start time={}", start);
        while (recordsSent < numRecords) {
            String messageStr = "Message_" + messageKey;
            // long startTime = System.currentTimeMillis();
            if (isAsync) { // Send asynchronously
                ProducerRecord<Integer, String> record = new ProducerRecord<>(topic,
                        messageKey,
                        messageStr);
                consumer.accept(record);
//                    SendAsynchronously(messageKey, messageStr, startTime);

            } else { // Send synchronously
                try {
                    producer.send(new ProducerRecord<>(topic,
                            messageKey,
                            messageStr)).get();
                    //  System.out.println("Sent message: (" + messageKey + ", " + messageStr + ")");
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            messageKey += 2;
            recordsSent += 1;
            if (recordsSent % 1000 == 0) {
                log.info("record send size={}", recordsSent);
            }
        }
        long end = System.currentTimeMillis();
        int avg = (int) (numRecords / (end - start));
        log.info("Producer sent {} records successfully,endTime={},tps={}", numRecords, end, avg);
        latch.countDown();
    }

    private void SendAsynchronously(ProducerRecord<Integer, String> record, int messageKey, String messageStr, long startTime) {

        producer.send(record, new DemoCallBack(startTime, messageKey, messageStr));
    }

    public static class DemoCallBack implements Callback {

        private final long startTime;
        private final int key;
        private final String message;

        public DemoCallBack(long startTime, int key, String message) {
            this.startTime = startTime;
            this.key = key;
            this.message = message;
        }

        /**
         * A callback method the user can implement to provide asynchronous handling of request completion. This method will
         * be called when the record sent to the server has been acknowledged. When exception is not null in the callback,
         * metadata will contain the special -1 value for all fields except for topicPartition, which will be valid.
         *
         * @param metadata  The metadata for the record that was sent (i.e. the partition and offset). An empty metadata
         *                  with -1 value for all fields except for topicPartition will be returned if an error occurred.
         * @param exception The exception thrown during processing of this record. Null if no error occurred.
         */
        public void onCompletion(RecordMetadata metadata, Exception exception) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (metadata != null) {
                System.out.println(
                        "message(" + key + ", " + message + ") sent to partition(" + metadata.partition() +
                                "), " +
                                "offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
            } else {
                exception.printStackTrace();
            }
        }
    }
}
