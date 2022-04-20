package com.wujt.kafka.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * @author wujt  2021/4/28
 * http://cloudurable.com/blog/kafka-tutorial-kafka-producer-advanced-java-examples/index.html
 */
public class SelfKafkaProducer {

    private static Producer<String, String>
    createProducer() {
        final Properties props = new Properties();
        // 设置server，key/value 序列化，ack 策略
        setupBootstrapAndSerializers(props);

        // 设置batch bytes，linger ms ，compression 压缩（consumer对应设置）
        setupBatchingAndCompression(props);
        // 设置重试策略和有序性设置
        // 请求超时时间，重试次数，重试时间间隔， 每个连接处理的最大请求数
        setupRetriesInFlightTimeout(props);

        //Install interceptor list - config "interceptor.classes"
        // props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,StockProducerInterceptor.class.getName());

        props.put("importantStocks", "IBM,UBER");

        return new KafkaProducer<>(props);
    }

    private static void setupBootstrapAndSerializers(Properties props) {
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                KafkaProperties.KAFKA_SERVER_ADDR);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "StockPriceKafkaProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());


        //Custom Serializer - config "value.serializer"
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());

        //Set number of acknowledgments - acks - default is all
        props.put(ProducerConfig.ACKS_CONFIG, "all");

    }

    private static void setupBatchingAndCompression(
            final Properties props) {
        props.put(ProducerConfig.LINGER_MS_CONFIG, 100);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16_384 * 4);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
    }

    private static void setupRetriesInFlightTimeout(Properties props) {
        //Only two in-flight messages per Kafka broker connection
        // - max.in.flight.requests.per.connection (default 5)
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,
                1);
        //Set the number of retries - retries
        props.put(ProducerConfig.RETRIES_CONFIG, 3);

        //Request timeout - request.timeout.ms
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 15_000);

        //Only retry after one second.
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1_000);
    }
}
