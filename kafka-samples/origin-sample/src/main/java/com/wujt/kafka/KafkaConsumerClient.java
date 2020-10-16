package com.wujt.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author wujt
 */
public class KafkaConsumerClient extends ShutdownableThread {
    private final KafkaConsumer<Integer, String> consumer;
    private final String topic;
    // 消费者组id
    private final String groupId;
    private final AtomicLong numMessageToConsume = new AtomicLong(0);
    private final CountDownLatch latch;

    public KafkaConsumerClient(final String topic,
                               final String groupId,
                               //   final Optional<String> instanceId,
                               final boolean readCommitted, CountDownLatch latch) {
        // super(name);
        super();
        this.groupId = groupId;
        Properties props = new Properties();
        // broker server addr
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_SERVER_ADDR);
        // group id
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        //  instanceId.ifPresent(id -> props.put(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, id));
        // auto commit default:
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        // auto commit interval
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        // session timeout
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        // key deserializer
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        // value deserializer
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        if (readCommitted) {
            props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        }
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        consumer = new KafkaConsumer<>(props);
        this.topic = topic;
        this.latch = latch;
    }

    @Override
    public void execute() {
        consumer.subscribe(Collections.singletonList(this.topic));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                // 等待一个处理一个批量的任务
                shutdown(1000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
        while (true) {
            ConsumerRecords<Integer, String> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<Integer, String> record : records) {
                System.out.println(groupId + " received message : from partition " + record.partition() + ", (" + record.key() + ", " + record.value() + ") at offset " + record.offset());
            }
            // todo 业务处理
            numMessageToConsume.accumulateAndGet(records.count(), Long::sum);
            if (!getRunning()) {
                System.out.println(groupId + " finished reading " + numMessageToConsume.get() + " messages");
                break;
            }
        }
        latch.countDown();
    }

}
