package com.cc.bus.topic;

import com.alibaba.fastjson.JSON;
import com.cc.bus.event.Subscription;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
class TopicTest {

    @Test
    @DisplayName("test Topic<String>")
    public void testTopicCreate() {
        // 利用String表明一个订阅者
        // 构建root
        Topic<String> root = Topic.createRoot();
        // 创建 /group/device/function/get topic
        Topic<String> topic = root.append("group").append("device").append("function").append("get");

        log.info("root topic={}", JSON.toJSONString(root.getTopic()));
        // subscribe root topic
        root.subscribe("subscribe root 1");
        root.subscribe("subscribe root 2");

        log.info("topic={} subscribe={}", root.getTopic(), root.getSubscribers());

        String topicName = "/";
        // 根据topic name 查找topic
        List<Topic<String>> topics = root.findTopic(topicName);
        topics.forEach(t -> {
            // 获取当前topic的订阅者
            log.info("{} subscribe={}", t.getTopic(), t.getSubscribers());
        });

        String topic1 = "/group";
        subscribeHandle(root, "/group");
        topic.subscribe("function get subscriber 1");
        subscribeHandle(root, "/group/device/function/get");

    }

    private void subscribeHandle(Topic<String> topic, String topic1) {
        topic.findTopic(topic1, t -> {
            t.getSubscribers().forEach(sub -> {
                log.info("topic={} subscriber={}", t.getTopic(), sub);
            });
        });
    }


    @Test
    @DisplayName("test Topic<Subscription>")
    public void testTopicSubscription() {
        // 利用Subscription 创建订阅（SubscribeInfo）
        String propHeart = "/group/device/properties/heart";
        String event = "/group/device/event/*";
        Subscription subscription = Subscription.builder()
                .subscriberId("props")
                .broker().local()  // set Future
                .topics(propHeart,event)
                .build();

        createSubscriber(subscription);


    }

    private void createSubscriber(Subscription subscription) {
        Topic<SubscribeInfo> root = Topic.createRoot();
        for (String topic : subscription.getTopics()) {

        }
    }
}
