package com.wujt.kafka.processor;

import com.wujt.kafka.consumer.KafkaConsumerClient;
import com.wujt.kafka.producer.KafkaProducerClient;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.ProducerFencedException;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * @author wujt must by kakfa > 2.5
 * <p>
 * <p>
 * kafka事务参考：
 * https://fleurer.github.io/2020/03/30/kafka02-transaction/
 * <p>
 * 精确一次语义实现
 * 原理：
 * 利用消费和业务处理保证原子性： 即消费kafka数据和业务处理完成并提交offset后才能算消费完成，否则回滚业务和本地消费的offset,再重新进行消费offset
 * 流转到kafka:
 * 利用kafka 生产者的事务机制，在提交事务推送后可提交事务，相应的将消费组的offset提交，同一事物
 * 当提交或者生产失败，回滚offset
 * <p>
 * 流转到其他场景：mysql
 * kafka消费者消费数据后，mysql业务处理加事务，当offset提交完成，事务完成。当过程中有一处问题，直接mysql回滚，kafka消费offset 重置
 */
public class ExactlyOnceMessageProcessor extends Thread {

    private static final boolean READ_COMMITTED = true;

    private final String inputTopic;
    private final String outputTopic;
    private final String transactionalId;
    private final String groupInstanceId;

    private final KafkaProducer<Integer, String> producer;
    private final KafkaConsumer<Integer, String> consumer;

    private final CountDownLatch latch;
    private String groupId;

    public ExactlyOnceMessageProcessor(final String inputTopic,
                                       final String outputTopic,
                                       final int instanceIdx,
                                       final CountDownLatch latch) {
        this.inputTopic = inputTopic;
        this.outputTopic = outputTopic;
        this.transactionalId = "Processor-" + instanceIdx;
        // It is recommended to have a relatively short txn timeout in order to clear pending offsets faster.
        final int transactionTimeoutMs = 10000;
        // A unique transactional.id must be provided in order to properly use EOS.


        KafkaProducerClient kafkaProducerClient = new KafkaProducerClient(outputTopic, "exactly-producer", true, transactionalId, true, -1, transactionTimeoutMs, null);
        producer = kafkaProducerClient.get();
        Consumer<ProducerRecord<Integer, String>> consumer = (record) -> {
            long startTime = System.currentTimeMillis();
            producer.send(record, new KafkaProducerClient.DemoCallBack(startTime, record.key(), record.value()));
        };
        kafkaProducerClient.setConsumer(consumer);
        // Consumer must be in read_committed mode, which means it won't be able to read uncommitted data.
        // Consumer could optionally configure groupInstanceId to avoid unnecessary rebalances.
        this.groupInstanceId = "Txn-consumer-" + instanceIdx;
        groupId = "Eos-consumer";
        this.consumer = new KafkaConsumerClient(inputTopic, groupId, Optional.empty(), READ_COMMITTED, false, latch, (value) -> {
            System.out.println("value:" + value);
        }).get();
        this.latch = latch;

    }

    @Override
    public void run() {
        // Init transactions call should always happen first in order to clear zombie transactions from previous generation.
        producer.initTransactions();

        final AtomicLong messageRemaining = new AtomicLong(Long.MAX_VALUE);

        consumer.subscribe(Collections.singleton(inputTopic), new ConsumerRebalanceListener() {

            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                printWithTxnId("Revoked partition assignment to kick-off rebalancing: " + partitions);
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                printWithTxnId("Received partition assignment after rebalancing: " + partitions);
                messageRemaining.set(messagesRemaining(consumer));
            }
        });

        int messageProcessed = 0;
        while (messageRemaining.get() > 0) {
            try {
                ConsumerRecords<Integer, String> records = consumer.poll(Duration.ofMillis(200));
                if (records.count() > 0) {
                    // Begin a new transaction session.
                    producer.beginTransaction();
                    for (ConsumerRecord<Integer, String> record : records) {
                        // Process the record and send to downstream.
                        ProducerRecord<Integer, String> customizedRecord = transform(record);
                        producer.send(customizedRecord);
                    }

                    Map<TopicPartition, OffsetAndMetadata> offsets = consumerOffsets();

                    // Checkpoint the progress by sending offsets to group coordinator broker.
                    // Note that this API is only available for broker >= 2.5.
                    // 该行代码在broker端实现为生产者生产成功，即提交offset到消费者组（事务性质）
                    // producer.sendOffsetsToTransaction(offsets, consumer.groupMetadata());
                    producer.sendOffsetsToTransaction(offsets, groupMetadata());

                    // Finish the transaction. All sent records should be visible for consumption now.
                    producer.commitTransaction();
                    messageProcessed += records.count();
                }
            } catch (ProducerFencedException e) {
                throw new KafkaException(String.format("The transactional.id %s has been claimed by another process", transactionalId));
//            } catch (FencedInstanceIdException e) {
//                throw new KafkaException(String.format("The group.instance.id %s has been claimed by another process", groupInstanceId));
            } catch (KafkaException e) {
                // If we have not been fenced, try to abort the transaction and continue. This will raise immediately
                // if the producer has hit a fatal error.
                producer.abortTransaction();

                // The consumer fetch position needs to be restored to the committed offset
                // before the transaction started.
                // 消费者重新设置消费的offset
                resetToLastCommittedPositions(consumer);
            }

            messageRemaining.set(messagesRemaining(consumer));
            printWithTxnId("Message remaining: " + messageRemaining);
        }

        printWithTxnId("Finished processing " + messageProcessed + " records");
        latch.countDown();
    }

    private String groupMetadata() {
        return groupId;
    }

    private Map<TopicPartition, OffsetAndMetadata> consumerOffsets() {
        Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
        for (TopicPartition topicPartition : consumer.assignment()) {
            offsets.put(topicPartition, new OffsetAndMetadata(consumer.position(topicPartition), null));
        }
        return offsets;
    }

    private void printWithTxnId(final String message) {
        System.out.println(transactionalId + ": " + message);
    }

    private ProducerRecord<Integer, String> transform(final ConsumerRecord<Integer, String> record) {
        printWithTxnId("Transformed record (" + record.key() + "," + record.value() + ")");
        return new ProducerRecord<>(outputTopic, record.key() / 2, "Transformed_" + record.value());
    }

    private long messagesRemaining(final KafkaConsumer<Integer, String> consumer) {
        final Map<TopicPartition, Long> fullEndOffsets = consumer.endOffsets(new ArrayList<>(consumer.assignment()));
        // If we couldn't detect any end offset, that means we are still not able to fetch offsets.
        if (fullEndOffsets.isEmpty()) {
            return Long.MAX_VALUE;
        }

        return consumer.assignment().stream().mapToLong(partition -> {
            long currentPosition = consumer.position(partition);
            printWithTxnId("Processing partition " + partition + " with full offsets " + fullEndOffsets);
            if (fullEndOffsets.containsKey(partition)) {
                return fullEndOffsets.get(partition) - currentPosition;
            }
            return 0;
        }).sum();
    }

    private static void resetToLastCommittedPositions(KafkaConsumer<Integer, String> consumer) {
        // 2.5 接口
        final Map<TopicPartition, OffsetAndMetadata> committed = getCommitted(consumer);
        consumer.assignment().forEach(tp -> {
            OffsetAndMetadata offsetAndMetadata = committed.get(tp);
            if (offsetAndMetadata != null)
                //seek(TopicPartition，long)指定新的位置
                consumer.seek(tp, offsetAndMetadata.offset());
            else
                //seekToBeginning，seekToEnd定位到最早或最近的offset
                consumer.seekToBeginning(Collections.singleton(tp));
        });
    }

    private static Map<TopicPartition, OffsetAndMetadata> getCommitted(KafkaConsumer<Integer, String> consumer) {
        return consumer.assignment().stream().collect(new Collector<TopicPartition, Map<TopicPartition, OffsetAndMetadata>, Map<TopicPartition, OffsetAndMetadata>>() {

            /**
             * 返回一个空的收集器
             * @return
             */
            @Override
            public Supplier<Map<TopicPartition, OffsetAndMetadata>> supplier() {
                return HashMap::new;
            }

            /**
             * 处理流元数据，并添加acc收集器
             * @return
             */
            @Override
            public BiConsumer<Map<TopicPartition, OffsetAndMetadata>, TopicPartition> accumulator() {
                return (Map<TopicPartition, OffsetAndMetadata> acc, TopicPartition topicPartition) -> {
                    OffsetAndMetadata offsetAndMetadata = consumer.committed(topicPartition);
                    acc.put(topicPartition, offsetAndMetadata);
                };
            }

            /**
             * 将两个收集器进行合并
             * @return
             */
            @Override
            public BinaryOperator<Map<TopicPartition, OffsetAndMetadata>> combiner() {
                return (map, map2) -> {
                    map.putAll(map2);
                    return map;
                };
            }

            /**
             * 累加器对象转换为整个集合操作的最终结果,这类收集器和返回结果一致，所以直接用identity()
             * @return
             */
            @Override
            public Function<Map<TopicPartition, OffsetAndMetadata>, Map<TopicPartition, OffsetAndMetadata>> finisher() {
                return Function.identity();
            }

            /**
             * UNORDERED—— 归约结果不受流中项目的遍历和累积顺序的影响。
             * CONCURRENT—— accumulator函数可以从多个线程同时调用,且该收集器可以并行执行。如果收集器没有标为UNORDERED，那 它仅在用于用于无序数据源时才可以并行归约。
             * IDENTITY_ FINISH—— 这表明完成器方法返回的函数是一个不改变的函数，这种情况下，累加器对象将会直接用作合并过程 的最终结果。
             * @return
             */
            @Override
            public Set<Characteristics> characteristics() {
                return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
            }
        });
    }
}
