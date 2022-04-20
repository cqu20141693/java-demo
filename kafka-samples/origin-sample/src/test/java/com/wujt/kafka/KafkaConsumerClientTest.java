package com.wujt.kafka;

import com.alibaba.fastjson.JSONObject;
import com.wujt.kafka.consumer.KafkaConsumerClient;
import com.wujt.kafka.config.KafkaProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author wujt
 */
@Slf4j
public class KafkaConsumerClientTest {

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    @Test
    public void testStart() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        KafkaConsumerClient chanConsumer = new KafkaConsumerClient(KafkaProperties.CHAN_CONN_TOPIC,
                "DemoConsumer",
                Optional.empty(),
                false,
                true,
                latch,
                (value) -> {
                });
        executor.submit(chanConsumer);

        consumer(KafkaProperties.DATA_TOPOC, latch, (object) -> {
            byte[] payloads = object.getBytes("payload");
            String originData = null;
            try {
                originData = new String(payloads, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            log.info("payload={},origin data={}", payloads, originData);
        });

    }

    private void consumer(String topic, CountDownLatch latch, Consumer<JSONObject> consumer) throws InterruptedException {
        KafkaConsumerClient dataConsumer = new KafkaConsumerClient(topic, "DemoConsumer", Optional.empty(), false, false, latch, (value) -> {
            JSONObject object = JSONObject.parseObject(value);
            consumer.accept(object);
        });
        executor.execute(dataConsumer);
        latch.await();
    }

    @Test
    public void consumerChecked() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        consumer("data-topic-checked", latch, (object) -> {
            log.info(",origin data={}", object.toJSONString());
        });
    }

    @Test
    public void consumerDataTopic() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        consumer("data-topic", latch, (object) -> {
            log.info(",origin data={}", object.toJSONString());
        });
    }

    @After
    public void after() {
        executor.shutdown();
    }
}