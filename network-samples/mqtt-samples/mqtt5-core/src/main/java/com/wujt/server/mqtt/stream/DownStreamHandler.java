package com.wujt.server.mqtt.stream;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;

/**
 * @author zc
 * @date 2019/10/24 16:02
 */
public interface DownStreamHandler {
    /**
     * 向某个设备发送PublishAck消息。 主要用于收到QoS1的消息时的处理
     *
     * @param clientId  deviceKey 设备标识
     * @param messageId PublishAck消息中的messageId
     * @return true标识成功
     */
    boolean replyPubAck(String clientId, int messageId);

    /**
     * pub qos 2 response
     *
     * @param clientId
     * @param messageId
     * @return
     */
    boolean replyPubRec(String clientId, int messageId);

    /**
     * pubRec response
     *
     * @param clientId
     * @param messageId
     * @return
     */
    boolean replyPubRel(String clientId, int messageId);

    /**
     * pubRel response
     *
     * @param clientId
     * @param messageId
     * @return
     */
    boolean replyPubComp(String clientId, int messageId);

    /**
     * 向某个设备发送ConnAck消息。
     *
     * @param channel
     * @param returnCode
     */
    ChannelFuture replyConnAck(Channel channel, MqttConnectReturnCode returnCode);

    /**
     * 向某个设备发送UnsubAck消息。
     *
     * @param channel
     * @param messageId
     */
    ChannelFuture replyUnsubAck(Channel channel, int messageId, Iterable<Short> reasons);

    /**
     * 向某个设备发送SubAck消息。
     *
     * @param channel
     * @param messageId
     */
    ChannelFuture replySubAck(Channel channel, int messageId, Iterable<Integer> grantedQoSLevels);

    /**
     * 向某个设备发送PingResp消息。
     *
     * @param channel
     */
    ChannelFuture replyPingResp(Channel channel);


    /**
     * 刷写链路可写数据
     *
     * @param channel
     */
    ChannelFuture flushChannelWrite(Channel channel);


    /**
     * 发送qos==0 的下行消息 ： error，welcome,proxyNotify
     *
     * @param channel
     * @param topic
     * @param msg
     * @return
     */
    ChannelFuture publishQos0(Channel channel, String topic, byte[] msg);

    /**
     * 发送qos=1 的消息: command 下发
     *
     * @param channel
     * @param topic
     * @param payload
     * @param session
     * @return
     */
    //   Boolean publishSessionQosMsg(ConnectionDescriptor channel, String topic, byte[] payload, QosEventInfo session);


}
