package com.wujt.config;

import com.wujt.producer.KafkaProducerListener;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.ExponentialBackOff;
import org.springframework.util.backoff.FixedBackOff;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author wujt
 * https://www.cnblogs.com/yx88/p/11013338.html
 */
@Configuration
@EnableKafka
public class KafkaConfig {

    private Map<String, Object> consumerConfigs(@NotNull KafkaConsumerProperties kafkaConsumerProperties) {
        Map<String, Object> props = new HashMap<>(16);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerProperties.getBootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConsumerProperties.getKeyDeserializer());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConsumerProperties.getValueDeserializer());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerProperties.getGroupId());
        //   props.put(ConsumerConfig.CLIENT_ID_CONFIG, kafkaConsumerProperties.getClientId());

        Optional.ofNullable(kafkaConsumerProperties.getEnableAutoCommit()).ifPresent(autoCommit -> props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autoCommit));
        Optional.ofNullable(kafkaConsumerProperties.getAutoCommitInterval()).ifPresent(commitInterval -> props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, commitInterval));
        Optional.ofNullable(kafkaConsumerProperties.getAutoOffsetReset()).ifPresent(offsetReset -> props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offsetReset));

        Optional.ofNullable(kafkaConsumerProperties.getFetchMinSize()).ifPresent(minSize -> props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, minSize));
        Optional.ofNullable(kafkaConsumerProperties.getFetchMaxWait()).ifPresent(maxWait -> props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, maxWait));
        Optional.ofNullable(kafkaConsumerProperties.getMaxPollRecords()).ifPresent(maxRecords -> props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxRecords));

        Optional.ofNullable(kafkaConsumerProperties.getHeartbeatInterval()).ifPresent(heartbeatInterval -> props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, heartbeatInterval));
        Optional.ofNullable(kafkaConsumerProperties.getMaxPollIntervalMs()).ifPresent(pollInterval -> props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, pollInterval));

        Optional.ofNullable(kafkaConsumerProperties.getPartitionAssignStrategy()).ifPresent(assignStrategy -> props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, assignStrategy));
        return props;
    }

    @Bean("kafkaConsumerFactory")
    public ConsumerFactory<Object, Object> kafkaConsumerFactory(KafkaConsumerProperties kafkaConsumerProperties) {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(kafkaConsumerProperties));
    }

    @Bean("manualConsumerFactory")
    public ConsumerFactory<String, String> manualConsumerFactory(KafkaConsumerProperties kafkaConsumerProperties) {
        kafkaConsumerProperties.setEnableAutoCommit(false);
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(kafkaConsumerProperties));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> manualSingleContainerFactory(
            @Qualifier("manualConsumerFactory") ConsumerFactory<String, String> manualConsumerFactory,
            KafkaTemplate<String, String> kafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(manualConsumerFactory);
        // 容器实现多线程 消费者
        factory.setConcurrency(1);
        factory.setErrorHandler(errorHandler(kafkaTemplate));
        ContainerProperties containerProperties = factory.getContainerProperties();
        containerProperties.setAckMode(ContainerProperties.AckMode.COUNT_TIME);
        containerProperties.setAckTime(500);
        containerProperties.setAckCount(500);
        containerProperties.setSyncCommits(true);

        return factory;
    }

    /**
     * 减少offset commit ,消费一批后进行提交
     *
     * @param manualConsumerFactory
     * @return
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> manualBatchContainerFactory(
            @Qualifier("manualConsumerFactory") ConsumerFactory<String, String> manualConsumerFactory,
            KafkaTemplate<String, String> kafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(manualConsumerFactory);
        // 容器实现多线程 消费者
        factory.setConcurrency(1);
        // 表示监听提可以批量获取poll 消息
        factory.setBatchListener(true);
        factory.setBatchErrorHandler(batchErrorHandler(kafkaTemplate));

        ContainerProperties containerProperties = factory.getContainerProperties();
        //设置拉取超时时间
        //  containerProperties.setPollTimeout(3000);

        //AckMode针对ENABLE_AUTO_COMMIT_CONFIG=false时生效,容器提供的ackMode
        containerProperties.setAckMode(ContainerProperties.AckMode.COUNT_TIME);
        containerProperties.setAckTime(500);
        containerProperties.setAckCount(500);
        // commit method use commitSync() or commitAsync()
        containerProperties.setSyncCommits(true);

        return factory;
    }

    /**
     * 指数时间的延迟backoff,直到设置的maxTime, 最大的延迟时间需要小于max-poll-interval
     * 当backoff 失败时，将消息写入死信队列
     *
     * @return
     */
    @Bean
    public BatchErrorHandler batchErrorHandler(KafkaTemplate<String, String> kafkaTemplate) {
        // 初始化1000,1000*2,1000*2*2
        ExponentialBackOff backOff = new ExponentialBackOff(1000L, 2);
        // 重试最大时间间隔, 需要小于最大请求间隔时间，否则服务端会认为客户端已经不可用
        backOff.setMaxElapsedTime(30000L);

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);
        return new RetryingBatchErrorHandler(backOff, recoverer);
    }


    /**
     * 重试小于最大次数时，重新投递该消息给 Consumer
     * 重试到达最大次数时，如果Consumer 还是消费失败时，该消息就会发送到死信队列。
     * 死信队列的 命名规则为： 原有 Topic + .DLT 后缀 = 其死信队列的 Topic
     *
     * @param kafkaTemplate
     * @return
     */
    @Bean
    public ErrorHandler errorHandler(KafkaTemplate<String, String> kafkaTemplate) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);
        // 固定时间补偿
        BackOff backOff = new FixedBackOff(10 * 1000L, 3L);

        return new SeekToCurrentErrorHandler(recoverer, backOff);
    }

    private Map<String, Object> producerConfigs(KafkaProducerProperties kafkaProducerProperties) {
        Map<String, Object> props = new HashMap<>(16);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerProperties.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerProperties.getKeySerializer());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerProperties.getValueSerializer());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProducerProperties.getClientId());

        props.put(ProducerConfig.ACKS_CONFIG, kafkaProducerProperties.getAcks());
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaProducerProperties.getRetries());
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaProducerProperties.getRequestTimeoutMs());
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, kafkaProducerProperties.getRetryInterval());

        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, kafkaProducerProperties.getMaxRequestPerConnection());

        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerProperties.getBatchSize());
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerProperties.getLingerMs());
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, kafkaProducerProperties.getCompressionType());


        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaProducerProperties.getBufferMemory());
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, kafkaProducerProperties.getMaxBlockMs());

        return props;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory(KafkaProducerProperties kafkaProducerProperties) {
        return new DefaultKafkaProducerFactory<>(producerConfigs(kafkaProducerProperties));
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory, ProducerListener<String, String> producerListener) {
        // autoFlush ，是否发送时自动进行flush调用,flush 调用对先将缓存区内的数据发送成，而不是直接发送到缓冲区
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory, false);
        kafkaTemplate.setProducerListener(producerListener);
        return kafkaTemplate;
    }

    @Bean
    public ProducerListener<String, String> producerListener() {
        return new KafkaProducerListener();
    }

}
