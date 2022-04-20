package com.wujt.kafka.config;

/**
 * @author wujt
 */
public class KafkaProperties {
    // 消费topic
    public static final String CHAN_CONN_TOPIC = "chan-conn-topic";
    public static final String DATA_TOPOC = "origin-data-topic";
    public static final String QOS_TOPIC = "qos-event-topic";
    //kafka broker dev 地址
//    public static final String KAFKA_SERVER_ADDR = "kafka-dev-outer-0.kafka-dev.svc.cluster.local:9092,kafka-dev-outer-1.kafka-dev.svc.cluster.local:9092,kafka-dev-outer-2.kafka-dev.svc.cluster.local:9092";
    //kafka broker cq local 地址  : 使用时需要替换wujt为公司
    public static String KAFKA_SERVER_ADDR = "kafka-dev-outer-0.kafka-dev.wujt.cq.in:9092," +
            "kafka-dev-outer-1.kafka-dev.wujt.cq.in:9092," +
            "kafka-dev-outer-2.kafka-dev.wujt.cq.in:9092";

}
