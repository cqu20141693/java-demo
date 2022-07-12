package com.wujt.kafka;

import com.alibaba.fastjson.JSONObject;
import com.wujt.kafka.admin.KafkaAdminClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.TopicDescription;
import org.junit.Test;

import java.util.Set;

/**
 * @author wujt
 */
@Slf4j
public class KafkaAdminClientTest {

    @Test
    public void listTopic() {
        Set<String> topic = KafkaAdminClient.listTopic();
        log.info("topics={}", JSONObject.toJSONString(topic));
    }

    @Test
    public void createTopic() {
        String topic = "test";
        int partition = 1;
        short replication = 1;
        assert KafkaAdminClient.createTopic(topic, partition, replication, null);

        assert KafkaAdminClient.increasePartitions(topic, partition + 1);

        assert KafkaAdminClient.delTopic(topic);
    }

    @Test
    public void increasePartitions() {
        String topic = "origin-data-topic";
        int partition = 2;
        KafkaAdminClient.increasePartitions(topic, partition);

        getDescription(topic);
    }

    private void getDescription(String topic) {
        TopicDescription topicDescription = KafkaAdminClient.descripteTopic(topic);
        log.info("topic description={}", JSONObject.toJSONString(topicDescription));
    }

    @Test
    public void rebuildTopic() {
        String topic = "origin-data-topic";
        int partition = 1;
        short replication = 1;
        //  assert KafkaAdminClient.delTopic(topic);
        assert KafkaAdminClient.createTopic(topic, partition, replication, null);
        getDescription(topic);
    }

}
