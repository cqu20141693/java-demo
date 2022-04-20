package com.wujt.kafka;

import com.wujt.kafka.config.KafkaProperties;
import com.wujt.kafka.consumer.KafkaConsumerClient;
import com.wujt.kafka.model.KafkaLocalInfo;
import com.wujt.kafka.processor.ExactlyOnceMessageProcessor;
import com.wujt.kafka.producer.KafkaProducerClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.TimeoutException;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.errors.UnknownTopicOrPartitionException;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author wujt
 */
@Slf4j
public class KafkaExactlyOnceTest {


    private static final String INPUT_TOPIC = "input-topic";
    private static final String OUTPUT_TOPIC = "output-topic";


    @Test
    public void test() throws ExecutionException, InterruptedException {

        KafkaLocalInfo kafkaLocalInfo = KafkaLocalInfo.builder().numPartitions(1).replicationFactor((short) 1).numInstances(1).numRecords(10).build();
        /* Stage 1: topic cleanup and recreation */
        recreateTopics(kafkaLocalInfo.getNumPartitions(), kafkaLocalInfo.getReplicationFactor());

        CountDownLatch prePopulateLatch = new CountDownLatch(1);

        /* Stage 2: pre-populate records */
        KafkaProducerClient producerClient = new KafkaProducerClient(INPUT_TOPIC, "pre-producer", false, null, true, kafkaLocalInfo.getNumRecords(), -1, prePopulateLatch);
        Consumer<ProducerRecord<Integer, String>> consumer = (record) -> {
            long startTime = System.currentTimeMillis();
            producerClient.get().send(record, new KafkaProducerClient.DemoCallBack(startTime, record.key(), record.value()));
        };
        producerClient.setConsumer(consumer);
        new Thread(producerClient, "pre-producer").start();


        if (!prePopulateLatch.await(5, TimeUnit.MINUTES)) {
            throw new TimeoutException("Timeout after 5 minutes waiting for data pre-population");
        }

        CountDownLatch transactionalCopyLatch = new CountDownLatch(kafkaLocalInfo.getNumInstances());

        /* Stage 3: transactionally process all messages */
        for (int instanceIdx = 0; instanceIdx < kafkaLocalInfo.getNumInstances(); instanceIdx++) {
            ExactlyOnceMessageProcessor messageProcessor = new ExactlyOnceMessageProcessor(
                    INPUT_TOPIC, OUTPUT_TOPIC, instanceIdx, transactionalCopyLatch);
            messageProcessor.start();
        }
        if (!transactionalCopyLatch.await(5, TimeUnit.MINUTES)) {
            throw new TimeoutException("Timeout after 5 minutes waiting for transactionally message copy");
        }

        CountDownLatch consumeLatch = new CountDownLatch(1);

        /* Stage 4: consume all processed messages to verify exactly once */
        KafkaConsumerClient consumerThread = new KafkaConsumerClient(OUTPUT_TOPIC, "Verify-consumer", Optional.empty(), true, false, consumeLatch, (value) -> {
        });
        new Thread(consumerThread).start();

        if (!consumeLatch.await(5, TimeUnit.MINUTES)) {
            throw new TimeoutException("Timeout after 5 minutes waiting for output data consumption");
        }

    }

    private static void recreateTopics(final int numPartitions, final short replicationFactor)
            throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                KafkaProperties.KAFKA_SERVER_ADDR);

        AdminClient adminClient = AdminClient.create(props);

        List<String> topicsToDelete = Arrays.asList(INPUT_TOPIC, OUTPUT_TOPIC);

        deleteTopic(adminClient, topicsToDelete);

        // Check topic existence in a retry loop
        while (true) {
            System.out.println("Making sure the topics are deleted successfully: " + topicsToDelete);

            Set<String> listedTopics = adminClient.listTopics().names().get();
            System.out.println("Current list of topics: " + listedTopics);

            boolean hasTopicInfo = false;
            for (String listedTopic : listedTopics) {
                if (topicsToDelete.contains(listedTopic)) {
                    hasTopicInfo = true;
                    break;
                }
            }
            if (!hasTopicInfo) {
                break;
            }
            Thread.sleep(1000);
        }

        // Create topics in a retry loop
        while (true) {
            final List<NewTopic> newTopics = Arrays.asList(
                    new NewTopic(INPUT_TOPIC, numPartitions, replicationFactor),
                    new NewTopic(OUTPUT_TOPIC, numPartitions, replicationFactor));
            try {
                adminClient.createTopics(newTopics).all().get();
                System.out.println("Created new topics: " + newTopics);
                break;
            } catch (ExecutionException e) {
                if (!(e.getCause() instanceof TopicExistsException)) {
                    throw e;
                }
                System.out.println("Metadata of the old topics are not cleared yet...");

                deleteTopic(adminClient, topicsToDelete);

                Thread.sleep(1000);
            }
        }
    }

    private static void deleteTopic(final AdminClient adminClient, final List<String> topicsToDelete)
            throws InterruptedException, ExecutionException {
        try {
            adminClient.deleteTopics(topicsToDelete).all().get();
        } catch (ExecutionException e) {
            if (!(e.getCause() instanceof UnknownTopicOrPartitionException)) {
                throw e;
            }
            System.out.println("Encountered exception during topic deletion: " + e.getCause());
        }
        System.out.println("Deleted old topics: " + topicsToDelete);
    }
}
