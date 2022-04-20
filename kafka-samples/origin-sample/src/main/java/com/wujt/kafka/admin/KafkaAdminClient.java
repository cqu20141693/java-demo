package com.wujt.kafka.admin;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewPartitions;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * @author wujt
 */
@Slf4j
public class KafkaAdminClient {

    private static AdminClient adminClient;
    private static Properties props = new Properties();

    static {

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "10.200.95.239:9092,10.200.108.175:9092,10.200.105.174:9092");

        adminClient = AdminClient.create(props);
    }


    // 新建topic
    public static boolean createTopic(String name, int numPartitions, short replicationFactor,
                                      Map<String, String> configs) {
        AdminClient client = getAdminClient();
        if (client == null) {
            log.debug("admin client is null");
            return false;
        }

        Set<String> topics = null;
        try {
            topics = client.listTopics().names().get();
            if (topics.contains(name)) {
                log.debug("topic is exist");
                return true;
            }

            NewTopic topic = new NewTopic(name, numPartitions, replicationFactor);
            if (configs != null && !configs.isEmpty()) {
                topic.configs(configs);
            }

            client.createTopics(Lists.newArrayList(topic)).all().get();

            topics = client.listTopics().names().get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("创建topic:{}出错1", name, e);
        } catch (ExecutionException e) {
            log.error("创建topic:{}出错2", name, e);
        }

        if (topics == null) {
            return false;
        }
        return topics.contains(name);

    }

    // 删除topic
    public static boolean delTopic(String name) {
        AdminClient client = getAdminClient();
        if (client == null) {
            return false;
        }

        Set<String> topics = null;
        try {
            topics = client.listTopics().names().get();
            if (!topics.contains(name)) {
                return true;
            }

            client.deleteTopics(Lists.newArrayList(name)).all().get();

            topics = client.listTopics().names().get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("删除topic:{}出错1", name, e);
        } catch (ExecutionException e) {
            log.error("删除topic:{}出错2", name, e);
        }

        if (topics == null) {
            return true;
        }
        return !topics.contains(name);
    }

    /**
     * 增加partitions
     *
     * @param name
     * @param numPartitions
     * @return
     */
    public static boolean increasePartitions(String name, int numPartitions) {
        AdminClient client = getAdminClient();
        if (client == null) {
            return false;
        }

        try {

            // 获取当前分区数量
            Map<String, TopicDescription> stringTopicDescriptionMap = client.describeTopics(Lists.newArrayList(name)).all().get();
            int current = stringTopicDescriptionMap.get(name).partitions()
                    .size();

            if (numPartitions <= current) {
                return true;
            }

            NewPartitions partitions = NewPartitions.increaseTo(numPartitions);
            client.createPartitions(Collections.singletonMap(name, partitions)).all().get();
            current = client.describeTopics(Lists.newArrayList(name)).all().get().get(name).partitions()
                    .size();
            assert current == numPartitions;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("修改topic:{}的partitions数量:{}出错1", name, numPartitions, e);
        } catch (ExecutionException e) {
            log.error("修改topic:{}的partitions数量:{}出错2", name, numPartitions, e);
        }

        return true;
    }

    private static AdminClient getAdminClient() {
        return adminClient;
    }

    public static TopicDescription descripteTopic(String topicName) {
        AdminClient client = getAdminClient();
        if (client == null) {
            return null;
        }
        try {
            Map<String, TopicDescription> topicDescriptionMap = client.describeTopics(Lists.newArrayList(topicName)).all().get();
            return topicDescriptionMap.get(topicName);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("describeTopics occur interrupt exception");
        } catch (ExecutionException e) {
            log.error("describeTopics occur ExecutionException topicName={},e={}", topicName, e);
        }

        return null;
    }

    /**
     * 可以实现动态加载
     *
     * @return
     */
    private static Properties getProperties() {
        return props;
    }
}
