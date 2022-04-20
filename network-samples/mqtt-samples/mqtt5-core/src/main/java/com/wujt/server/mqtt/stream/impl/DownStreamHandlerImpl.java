package com.wujt.server.mqtt.stream.impl;

import com.wujt.server.mqtt.stream.DownStreamBase;
import com.wujt.server.mqtt.stream.DownStreamHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.netty.handler.codec.mqtt.MqttProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author wujt
 */
@Component
@Slf4j
public class DownStreamHandlerImpl extends DownStreamBase implements DownStreamHandler {
    @Override
    public boolean replyPubAck(String clientId, int messageId) {
        return false;
    }

    @Override
    public boolean replyPubRec(String clientId, int messageId) {
        return false;
    }

    @Override
    public boolean replyPubRel(String clientId, int messageId) {
        return false;
    }

    @Override
    public boolean replyPubComp(String clientId, int messageId) {
        return false;
    }

    @Override
    public ChannelFuture replyConnAck(Channel channel, MqttConnectReturnCode returnCode) {
        return mqttConnAck(channel, returnCode, MqttProperties.NO_PROPERTIES);
    }

    @Override
    public ChannelFuture replyUnsubAck(Channel channel, int messageId, Iterable<Short> reasons) {
        try {
            if (channel != null && channel.isWritable()) {
                return unsubAck(channel, messageId, MqttProperties.NO_PROPERTIES, reasons);
            } else {
                log.error("send sub ack failed. messageId = {}", messageId);
            }
        } catch (Exception e) {
            log.error("mqtt sub ack failed. messageId={}", messageId);
        }
        return null;
    }


    @Override
    public ChannelFuture replySubAck(Channel channel, int messageId, Iterable<Integer> grantedQoSLevels) {
        try {
            if (channel != null && channel.isWritable()) {
                return subAck(channel, messageId, MqttProperties.NO_PROPERTIES, grantedQoSLevels);
            } else {
                log.error("send sub ack failed. messageId = {}", messageId);
            }
        } catch (Exception e) {
            log.error("mqtt sub ack failed. messageId={}", messageId);
        }
        return null;
    }

    @Override
    public ChannelFuture replyPingResp(Channel channel) {
        try {
            if (channel != null && channel.isWritable()) {
                return pingResp(channel);
            } else {
                log.error("send ping resp failed.");
            }
        } catch (Exception e) {
            log.error("mqtt ping resp failed.");
        }
        return null;
    }

    @Override
    public ChannelFuture flushChannelWrite(Channel channel) {
        return null;
    }

    @Override
    public ChannelFuture publishQos0(Channel channel, String topic, byte[] msg) {
        return null;
    }
}
