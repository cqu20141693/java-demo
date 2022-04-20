package com.wujt.server.mqtt.stream;

import com.wujt.server.netty.NettyUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.function.BooleanSupplier;

import static io.netty.handler.codec.mqtt.MqttQoS.AT_MOST_ONCE;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 下行操作基础类
 *
 * @author wujt
 */
@Slf4j
public abstract class DownStreamBase {
    private static final Logger logger = LoggerFactory.getLogger(DownStreamBase.class);

    protected ChannelFuture mqttPush(Channel channel, String topic, byte[] payload, int qos, int packetId, BooleanSupplier actionBeforePush) {

        if (channel != null && !StringUtils.isEmpty(topic)) {
            try {
                MqttQoS mqttQoS = MqttQoS.valueOf(qos);
                MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PUBLISH, false, mqttQoS, false, 0);
                MqttPublishVariableHeader varHeader = new MqttPublishVariableHeader(topic, packetId);
                ByteBuf byteBuf = Unpooled.wrappedBuffer(payload);
                MqttPublishMessage publishMessage = new MqttPublishMessage(fixedHeader, varHeader, byteBuf);
                switch (mqttQoS) {
                    case AT_MOST_ONCE:
                        break;
                    case AT_LEAST_ONCE:
                    case EXACTLY_ONCE:
                        // 开启定时发送任务，当收到ack时停止,网络延迟大情况会导致内存暴涨
                        // 建议将qos>0的消息利用上层感知而非异步处理
                        // 或者上层处理，ack，expired...事件
                        break;
                }
                boolean writable = channel.isWritable();
                logger.debug("clientId={} topic={} payload={} qos={},writable={}", NettyUtils.clientId(channel), topic, new String(payload, UTF_8), qos, writable);
                if (channel.isActive() && writable) {
                    if (actionBeforePush != null && !actionBeforePush.getAsBoolean()) {
                        //存在推送前的操作定义，且操作不成功
                        return null;
                    }
                    return channel.writeAndFlush(publishMessage);
                } else {  // 不可写的数据丢弃
                    logger.error("the buffer cannot be written,isWritable={}", channel.isWritable());
                    return null;
                }
            } catch (Exception e) {
                logger.error("mqtt push failed. topic={}", topic);
            }
        }
        logger.error("send failed,chan={},topic={}", channel, topic);
        return null;
    }

    ChannelFuture mqttPubAck(Channel channel, int messageId) {
        try {
            if (channel != null && channel.isWritable()) {
                MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBACK, false, AT_MOST_ONCE,
                        false, 0);
                MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(messageId);
                MqttPubAckMessage pubAckMessage = new MqttPubAckMessage(mqttFixedHeader, variableHeader);
                return channel.writeAndFlush(pubAckMessage);
            } else {
                logger.error("send pub ack failed. because the write buf is full. messageId = {}", messageId);
            }
        } catch (Exception e) {
            logger.error("mqtt push ack failed. messageId={}", messageId);
        }
        return null;
    }

    protected ChannelFuture mqttPubRec(Channel channel, int messageId) {
        try {
            if (channel != null && channel.isWritable()) {
                MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBREC, false, AT_MOST_ONCE,
                        false, 0);
                MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(messageId);
                MqttMessage pubAckMessage = new MqttMessage(mqttFixedHeader, variableHeader);
                return channel.writeAndFlush(pubAckMessage);
            } else {
                logger.error("send pub ack failed. because the write buf is full. messageId = {}", messageId);
            }
        } catch (Exception e) {
            logger.error("mqtt push ack failed. messageId={}", messageId);
        }
        return null;
    }

    protected ChannelFuture mqttPubRel(Channel channel, int messageId) {
        try {
            if (channel != null && channel.isWritable()) {
                MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBREL, false, AT_MOST_ONCE,
                        false, 0);
                MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(messageId);
                MqttMessage pubAckMessage = new MqttMessage(mqttFixedHeader, variableHeader);
                return channel.writeAndFlush(pubAckMessage);
            } else {
                logger.error("send pub ack failed. because the write buf is full. messageId = {}", messageId);
            }
        } catch (Exception e) {
            logger.error("mqtt push ack failed. messageId={}", messageId);
        }
        return null;
    }

    protected ChannelFuture mqttPubComp(Channel channel, int messageId) {
        try {
            if (channel != null && channel.isWritable()) {
                MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBCOMP, false, AT_MOST_ONCE,
                        false, 0);
                MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(messageId);
                MqttPubAckMessage pubAckMessage = new MqttPubAckMessage(mqttFixedHeader, variableHeader);
                return channel.writeAndFlush(pubAckMessage);
            } else {
                logger.error("send pub ack failed. because the write buf is full. messageId = {}", messageId);
            }
        } catch (Exception e) {
            logger.error("mqtt push ack failed. messageId={}", messageId);
        }
        return null;
    }

    protected ChannelFuture mqttConnAck(Channel channel, MqttConnectReturnCode returnCode, MqttProperties properties) {
        try {
            if (channel != null && channel.isWritable()) {
                MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.CONNACK, false, AT_MOST_ONCE,
                        false, 0);
                // VariableHeader
                // 1. 连接确认标志 位7-1是保留位且必须设置为0;第0（SP）位是会话存在标志(连接侧并未做Session)
                // 2. Connect Reason Code
                // 3. Properties:
//                MqttProperties properties = new MqttProperties();
//                MqttPropsUtils.setMaxQos(properties, (byte) MqttQoS.AT_LEAST_ONCE.value());
                MqttConnAckVariableHeader mqttConnAckVariableHeader = new MqttConnAckVariableHeader(returnCode, false, properties);
                // payload 为空
                MqttConnAckMessage mqttConnAckMessage = new MqttConnAckMessage(mqttFixedHeader, mqttConnAckVariableHeader);
                return channel.writeAndFlush(mqttConnAckMessage);
            } else {
                logger.error("send conn ack failed. returnCode = {}", returnCode);
            }
        } catch (Exception e) {
            logger.error("mqtt conn ack failed. returnCode={}", returnCode);
        }
        return null;
    }

    protected ChannelFuture unsubAck(Channel channel, int messageId, MqttProperties properties, Iterable<Short> reasons) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.UNSUBACK, false, AT_MOST_ONCE,
                false, 0);
        MqttMessageIdAndPropertiesVariableHeader variableHeader = new MqttMessageIdAndPropertiesVariableHeader(messageId, properties);
        // 原因码列表
        MqttUnsubAckPayload payload = new MqttUnsubAckPayload(reasons);
        MqttUnsubAckMessage unsubAckMessage = new MqttUnsubAckMessage(mqttFixedHeader, variableHeader, payload);
        return channel.writeAndFlush(unsubAckMessage);
    }


    protected ChannelFuture subAck(Channel channel, int messageId, MqttProperties properties, Iterable<Integer> grantedQoSLevels) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.SUBACK, false, AT_MOST_ONCE,
                false, 0);
        MqttMessageIdAndPropertiesVariableHeader variableHeader = new MqttMessageIdAndPropertiesVariableHeader(messageId, properties);
        // 原因码列表
        MqttSubAckPayload payload = new MqttSubAckPayload(grantedQoSLevels);
        MqttSubAckMessage ackMessage = new MqttSubAckMessage(mqttFixedHeader, variableHeader, payload);
        return channel.writeAndFlush(ackMessage);
    }


    protected ChannelFuture pingResp(Channel channel) {
        MqttFixedHeader pingHeader = new MqttFixedHeader(MqttMessageType.PINGRESP, false, AT_MOST_ONCE,
                false, 0);
        MqttMessage pingResp = new MqttMessage(pingHeader);
        return channel.writeAndFlush(pingResp);

    }
}
