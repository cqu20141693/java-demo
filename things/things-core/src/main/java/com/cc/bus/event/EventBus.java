package com.cc.bus.event;

import com.cc.things.codec.Codecs;
import com.cc.things.codec.Decoder;
import com.cc.things.codec.Encoder;
import com.cc.things.codec.Payload;
import com.cc.things.codec.defaults.DirectCodec;

/**
 * 基于订阅发布的事件总线,可用于事件传递,消息转发等.
 * wcc 2022/6/4
 */
public interface EventBus {

    /**
     * 从事件总线中订阅事件
     * <p>
     * 特别注意!!!
     * <p>
     * 如果没有调用
     * {@link TopicPayload#bodyToString()},
     * {@link TopicPayload#bodyToJson()},
     * {@link TopicPayload#bodyToJsonArray()},
     * {@link TopicPayload#getBytes()}
     * 使用TopicPayload后需要手动调用{@link TopicPayload#release()}释放.
     *
     * @param subscription 订阅信息
     * @return 事件流
     */
    TopicPayload subscribe(Subscription subscription);

    /**
     * 从事件总线中订阅事件,并按照指定的解码器进行数据转换
     *
     * @param subscription 订阅信息
     * @param decoder      解码器
     * @param <T>          解码后结果类型
     * @return 事件流
     */
    <T> T subscribe(Subscription subscription, Decoder<T> decoder);


    /**
     * 订阅主题并将事件数据转换为指定的类型
     *
     * @param subscription 订阅信息
     * @param type         类型
     * @param <T>          类型
     * @return 事件流
     */
    default <T> T subscribe(Subscription subscription, Class<T> type) {
        return subscribe(subscription, Codecs.lookup(type));
    }

    /**
     * 推送单个数据到事件总线中,并指定编码器用于将事件数据进行序列化
     *
     * @param topic   主题
     * @param encoder 编码器
     * @param event   事件数据
     * @param <T>     事件类型
     * @return 订阅者数量
     */
    <T> Long publish(String topic, Encoder<T> encoder, T event);


    /**
     * 推送单个数据到事件流中,默认自动根据事件类型进行序列化
     *
     * @param topic 主题
     * @param event 事件数据
     * @param <T>   事件类型
     * @return 订阅者数量
     */
    @SuppressWarnings("all")
    default <T> Long publish(String topic, T event) {
        if (event instanceof Payload) {
            return publish(topic, ((Payload) event));
        }
        return publish(topic, Codecs.lookup(event.getClass()), event);
    }

    default Long publish(String topic, Payload event) {
        return publish(topic, DirectCodec.INSTANCE, event);
    }

}
