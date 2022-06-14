package com.cc.bus.event;

import com.cc.bus.topic.TopicUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订阅，可以同时订阅多个topic
 * wcc 2022/6/4
 *
 * @see com.cc.bus.topic.SubscribeInfo
 */
@AllArgsConstructor
@Getter
public class Subscription implements Serializable {
    private static final long serialVersionUID = -6849794470754667710L;

    public static final Feature[] DEFAULT_FEATURES = Feature.values();

    //订阅者标识
    private final String subscriber;

    //订阅主题,主题以/分割,如: /device/TS-01/09012/message 支持通配符 /device/**
    private final String[] topics;

    // 消息路由消费者
    private final SubscribeHandler handler;
    //订阅特性
    private final Feature[] features;

    private Runnable doOnSubscribe;

    public static Subscription of(String subscriber, String... topic) {
        return Subscription
                .builder()
                .subscriberId(subscriber)
                .topics(topic)
                .build();
//        return new Subscription(subscriber, topic, DEFAULT_FEATURES, null);
    }

    public static Subscription of(String subscriber, String[] topic, Feature... features) {
        return Subscription
                .builder()
                .subscriberId(subscriber)
                .topics(topic)
                .features(features)
                .build();
    }

    public static Subscription of(String subscriber, String topic, Feature... features) {
        return Subscription
                .builder()
                .subscriberId(subscriber)
                .topics(topic)
                .features(features)
                .build();
        //return new Subscription(subscriber, new String[]{topic}, features, null);
    }

    public Subscription copy(Feature... newFeatures) {
        return new Subscription(subscriber, topics, handler, newFeatures, null);
    }

    public Subscription onSubscribe(Runnable sub) {
        this.doOnSubscribe = sub;
        return this;
    }

    @Getter
    public enum Feature {

        //如果相同的订阅者,只有一个订阅者收到消息
        shared("shared"),
        //订阅本地消息
        local("订阅本地消息"),
        //订阅来自代理的消息
        broker("订阅代理消息");

        Feature(String text) {
            this.text = text;
        }

        private final String text;

        public String getValue() {
            return name();
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        //订阅者标识
        private String subscriber;

        //订阅主题,主题以/分割,如: /device/TS-01/09012/message 支持通配符 /device/**
        private final Set<String> topics = new HashSet<>();

        //订阅特性
        private final Set<Feature> features = new HashSet<>();

        private SubscribeHandler handler;

        private Runnable doOnSubscribe;

        public Builder randomSubscriberId() {
            return subscriberId(UUID.randomUUID().toString());
        }

        public Builder subscriberId(String id) {
            this.subscriber = id;
            return this;
        }

        public Builder topics(String... topics) {
            return topics(Arrays.asList(topics));
        }

        public Builder topics(Collection<String> topics) {
            this.topics.addAll(topics.stream()
                    .flatMap(topic -> TopicUtils.expand(topic).stream())
                    .collect(Collectors.toSet()));
            return this;
        }

        public Builder features(Feature... features) {
            this.features.addAll(Arrays.asList(features));
            return this;
        }

        public Builder doOnSubscribe(Runnable runnable) {
            this.doOnSubscribe = runnable;
            return this;
        }

        public Builder justLocal() {
            this.features.clear();
            return features(Feature.local);
        }

        public Builder justBroker() {
            this.features.clear();
            return features(Feature.broker);
        }

        public Builder local() {
            return features(Feature.local);
        }

        public Builder broker() {
            return features(Feature.broker);
        }

        public Builder shared() {
            return features(Feature.shared);
        }

        public Subscription build() {
            if (features.isEmpty()) {
                local();
            }
            Assert.notEmpty(topics, "topic cannot be empty");
            Assert.hasText(subscriber, "subscriber cannot be empty");
            return new Subscription(subscriber, topics.toArray(new String[0]), handler, features.toArray(new Feature[0]), doOnSubscribe);
        }

    }
}
