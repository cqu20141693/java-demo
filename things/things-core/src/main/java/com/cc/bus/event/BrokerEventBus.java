package com.cc.bus.event;

import com.cc.bus.topic.SubscribeInfo;
import com.cc.bus.topic.Topic;
import com.cc.things.codec.Decoder;
import com.cc.things.codec.Encoder;
import com.cc.util.EnumDict;
import lombok.Data;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * broker 事件总线
 * wcc 2022/6/4
 */
@Data
public class BrokerEventBus implements EventBus {

    private final Topic<SubscribeInfo> root = Topic.createRoot();

    // 调度线程池， 事件发布时接收事件处理，将publish后的任务处理作为任务丢到线程池处理
    private int threadSize = Runtime.getRuntime().availableProcessors();
    ExecutorService service = new ThreadPoolExecutor(threadSize, threadSize * 2, 5, TimeUnit.MINUTES, new ArrayBlockingQueue<>(100000));

    @Override
    public TopicPayload subscribe(Subscription subscription) {
        for (String topic : subscription.getTopics()) {
            Topic<SubscribeInfo> node = root.append(topic);
            SubscribeInfo info = new SubscribeInfo();
            info.setSubscriberId(subscription.getSubscriber());
            info.setBroker(false);
            info.setFeatures(EnumDict.toMask(subscription.getFeatures()));
            info.setHandler(subscription.getHandler());
            node.subscribe(info);
        }
        return null;
    }

    @Override
    public <T> T subscribe(Subscription subscription, Decoder<T> decoder) {
        return null;
    }

    @Override
    public <T> Long publish(String topic, Encoder<T> encoder, T event) {

        return null;
    }
}
