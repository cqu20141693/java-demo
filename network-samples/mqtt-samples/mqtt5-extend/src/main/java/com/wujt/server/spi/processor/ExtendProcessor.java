package com.wujt.server.spi.processor;

import com.wujt.server.mqtt.domain.client.MqttClientInfo;
import com.wujt.server.mqtt.domain.evnet.QosEventInfo;
import io.netty.handler.codec.mqtt.MqttQoS;

import java.util.concurrent.CompletableFuture;

/**
 * @author wujt
 */
public interface ExtendProcessor {
    /**
     * 设备连接扩展处理,支持设备代理
     *
     * @param clientInfo 连接客户端的信息
     * @param timeStamp
     */
    CompletableFuture<?> onDeviceConnected(MqttClientInfo clientInfo, long timeStamp);

    /**
     * 设备断连扩展处理,支持设备代理
     *
     * @param clientInfo 连接客户端的信息
     * @param timeStamp
     */
    void onDeviceDisconnected(MqttClientInfo clientInfo, long timeStamp);

    /**
     * 设备在线扩展处理,支持设备代理
     *
     * @param clientInfo 连接客户端的信息
     */
    void onDeviceOnline(MqttClientInfo clientInfo, long timeStamp);

    /**
     * channel isWritable
     *
     * @param clientInfo
     */
    void onChanWritable(MqttClientInfo clientInfo, long timeStamp);

    /**
     * 当平台收到设备侧Publish消息时，会回调此方法.
     * <p>
     * 如果qos = 1, 则本方法有义务调用 BrokerService的sendPublishAck方法,向对应的客户端发送Publish Ack消息
     * <p>
     *
     * @param qos
     * @param topic
     * @param payload
     * @param clientInfo
     * @param timeMillis
     * @param dup
     * @param retain
     * @return
     */
    CompletableFuture<?> onReceivePublishMessage(MqttQoS qos, String topic, byte[] payload, MqttClientInfo clientInfo, long timeMillis, boolean dup, boolean retain);

    /**
     * 当平台收到设备侧的publish ack消息时，会回调此方法,
     * 协议中处理messageId的一次性任务完成确认和重用处理
     *
     * @param deviceKey  设备key
     * @param deviceId   设备id
     * @param clientInfo clientInfo
     * @param messageId  当qos为1时此值为有效值，否则为-1， mqtt publish消息中的messageId
     */
    CompletableFuture<?> onReceivePublishAckMessage(String deviceKey, int deviceId, MqttClientInfo clientInfo, int messageId);

    /**
     * 当平台把消息分发给某个订阅者时，回调此方法
     *
     * @param clientId   接受此消息的clientId
     * @param clientInfo 接受此消息client
     * @param messageId  当qos为1时此值为有效值，否则为-1， mqtt publish消息中的messageId
     * @param topic      本消息被publish到平台时的topic, 发布者发布时的topic
     * @param filter     本消息匹配的filter, 订阅者订阅的filter
     * @param qos        消息的qos
     * @param payload    消息的payload
     */
    void onRoutePublishMessage(String clientId, MqttClientInfo clientInfo, int messageId, String topic, String filter, int qos, byte[] payload);


    /**
     * 当平台收到订阅请求时，回调此方法
     *
     * @param clientId   发送订阅请求的clientId
     * @param clientInfo 发送订阅请求的client
     * @param filter     订阅请求中的filter
     * @param qos        订阅请求中的qos
     */
    void onReceiveSubscriptionReq(String clientId, MqttClientInfo clientInfo, String filter, int qos);

    /**
     * 当平台收到取消订阅请求时，回调此方法
     *
     * @param clientId   发送取消订阅请求的clientId
     * @param clientInfo 发送取消订阅请求的client
     * @param filter     取消订阅的filter
     * @param qos        取消订阅的qos
     */
    void onReceiveUnsubscriptionReq(String clientId, MqttClientInfo clientInfo, String filter, int qos);

    /**
     * send publish qos 1，2 event
     *
     * @param qosEventInfo
     */
    void onQosEvent(QosEventInfo qosEventInfo);
}
