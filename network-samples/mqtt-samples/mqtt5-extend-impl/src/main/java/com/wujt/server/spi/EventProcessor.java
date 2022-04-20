package com.wujt.server.spi;


import com.wujt.server.model.ChannelEventInfo;
import com.wujt.server.model.PublishInfo;
import com.wujt.server.mqtt.domain.evnet.QosEventInfo;

import java.util.concurrent.CompletableFuture;

/**
 * @author wujt
 */
public interface EventProcessor<T> {

    /**
     * qos1  send,ack,expired 通知： type,businessId,status  ： rabbitmq
     *
     * @param deviceKey
     * @param msg       {@link QosEventInfo}
     * @param needAsync
     */
    CompletableFuture<T> sendQosEvent(String deviceKey, QosEventInfo msg, boolean needAsync);

    /**
     * 发送channel连接，心跳，断开事件
     * <p>
     * 公有云实现方式为Kafka
     *
     * @param deviceKey
     * @param msg       {@link ChannelEventInfo}
     * @param needAsync
     * @return
     */
    CompletableFuture<T> sendChannelEvent(String deviceKey, ChannelEventInfo msg, boolean needAsync);

    /**
     * 发送 channel publish 报文消息
     *
     * @param deviceKey
     * @param msg       {@link PublishInfo}
     * @param needAsync
     */
    CompletableFuture<T> sendPublishEvent(String deviceKey, PublishInfo msg, Boolean needAsync);
}
