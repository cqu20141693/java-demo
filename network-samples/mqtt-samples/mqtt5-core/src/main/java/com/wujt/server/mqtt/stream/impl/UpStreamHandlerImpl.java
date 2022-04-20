package com.wujt.server.mqtt.stream.impl;

import com.wujt.config.MqttProtocolConfig;
import com.wujt.server.mqtt.domain.client.MqttClientInfo;
import com.wujt.server.mqtt.domian.ConnectionDescriptor;
import com.wujt.server.mqtt.domian.status.ConnectStatusHandler;
import com.wujt.server.mqtt.domian.store.relation.ConnectRelation;
import com.wujt.server.executor.ProcessExecutorGroup;
import com.wujt.server.mqtt.stream.DownStreamHandler;
import com.wujt.server.mqtt.stream.UpStreamHandler;
import com.wujt.server.mqtt.util.MqttPropsUtils;
import com.wujt.server.mqtt.util.topic.SystemTopicEnum;
import com.wujt.server.netty.NettyUtils;
import com.wujt.server.spi.processor.ExtendProcessor;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static io.netty.handler.codec.mqtt.MqttConnectReturnCode.CONNECTION_ACCEPTED;
import static io.netty.handler.codec.mqtt.MqttConnectReturnCode.CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION;

/**
 * @author wujt
 */
@Component
@Slf4j
public class UpStreamHandlerImpl implements UpStreamHandler {

    @Autowired
    private DownStreamHandler downStreamHandler;

    @Autowired
    private MqttProtocolConfig mqttConfig;
    @Autowired
    private ConnectRelation connectRelation;
    @Autowired
    private ConnectStatusHandler connectStatusHandler;
    @Autowired
    private ProcessExecutorGroup processExecutorGroup;
    @Autowired
    private ExtendProcessor extendProcessor;

    @Override
    public void handleConnect(Channel channel, MqttConnectMessage msg) {

        log.debug("mqtt connect occur,chan={}", channel);
        MqttFixedHeader fixedHeader = msg.fixedHeader();
        MqttMessageType messageType = fixedHeader.messageType();
        // 第四位保留，全为0，只有publish 报文时才有作用
//        boolean dup = fixedHeader.isDup();
//        boolean retain = fixedHeader.isRetain();
//        MqttQoS mqttQoS = fixedHeader.qosLevel();

        MqttConnectVariableHeader connectVariableHeader = msg.variableHeader();
        // Protocol Name,Level
        String name = connectVariableHeader.name();
        int version = connectVariableHeader.version();
        //connect flags
        boolean hasUserName = connectVariableHeader.hasUserName();
        boolean hasPassword = connectVariableHeader.hasPassword();
        // session
        boolean cleanSession = connectVariableHeader.isCleanSession();
        // will
        boolean willFlag = connectVariableHeader.isWillFlag();
        boolean willRetain = connectVariableHeader.isWillRetain();
        int willQos = connectVariableHeader.willQos();
        // Keep Alive
        int keepAliveTimeSeconds = connectVariableHeader.keepAliveTimeSeconds();

        // properties
        MqttProperties properties = connectVariableHeader.properties();
        boolean empty = properties.isEmpty();
        properties.getProperties(1);
        Integer sessionExpiryInterval = MqttPropsUtils.getSessionExpiryInterval(properties);


        MqttConnectPayload payload = msg.payload();
        String clientIdentifier = payload.clientIdentifier();
        MqttProperties willProperties = payload.willProperties();
        String userName = payload.userName();
        byte[] passwordInBytes = payload.passwordInBytes();
        String willTopic = payload.willTopic();
        byte[] willMessageInBytes = payload.willMessageInBytes();


        if (MqttVersion.MQTT_5.protocolLevel() != version || MqttVersion.MQTT_5.protocolName().equals(name)) {
            log.error("processConnect. MQTT protocol version is not valid. clientIdentifier={}", clientIdentifier);
            asyncCloseChannel(downStreamHandler.replyConnAck(channel, CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION));
            return;
        }
        // 后续校验失败需要告知其失败原因，上面两条后续再定
        if (keepAliveTimeSeconds < mqttConfig.getMinHeartBeatSecond()) {
            log.warn("processConnect. the keepAlive second is less then {}, the clientIdentifier = {} is forbidden to connect to this server"
                    , mqttConfig.getMinHeartBeatSecond(), clientIdentifier);
            String message = "the heartbeat second must >= " + mqttConfig.getMinHeartBeatSecond();
            asyncCloseChannel(downStreamHandler.publishQos0(channel,
                    SystemTopicEnum.ERROR.getName(), message.getBytes()));
            return;
        }

        //鉴权，初始化本地关系和基本信息

        // 可做回复
        ChannelFuture channelFuture = downStreamHandler.replyConnAck(channel, CONNECTION_ACCEPTED);
    }

    private void asyncCloseChannel(ChannelFuture channelFuture) {
        if (channelFuture != null) {
            channelFuture.addListener((ChannelFutureListener) future -> channelFuture.channel().close());
        }
    }

    @Override
    public void handlePublish(Channel channel, MqttPublishMessage msg) {
        String clientId = NettyUtils.clientId(channel);
        if (StringUtils.isEmpty(clientId)) {
            log.error("channel {} clientId is Empty!", channel);
            String message = "clientId is empty";
            connectStatusHandler.disconnect(channel, message);
            return;
        }
        ConnectionDescriptor connectionDescriptor = connectRelation.getConnByClientId(clientId);
        if (connectionDescriptor == null) {
            log.info("handlePublish: clientId={} , can't find related conn", clientId);
            String cause = "server connection description error";
            connectStatusHandler.disconnect(channel, cause);
            return;
        }

        //fix header
        MqttFixedHeader fixedHeader = msg.fixedHeader();
        // 是否重新发送
        boolean dup = fixedHeader.isDup();
        // 服务质量：0,1,2
        MqttQoS mqttQoS = fixedHeader.qosLevel();
        // 保留消息：发布者不定期发送状态消息这个场景
        boolean retain = fixedHeader.isRetain();
        // 控制报文类型
        MqttMessageType messageType = fixedHeader.messageType();


        // variable header
        MqttPublishVariableHeader variableHeader = msg.variableHeader();
        int packetId = variableHeader.packetId();
        String topicName = variableHeader.topicName();
        MqttProperties properties = variableHeader.properties();

        final byte[] payload = NettyUtils.readBytesAndRewind(msg.payload());
        // 消息接收的时间
        long timeMillis = System.currentTimeMillis();

        boolean result = processExecutorGroup.submitProcessTask(clientId, () -> {
            final MqttClientInfo clientInfo = connectionDescriptor.clientInfo();
            //返回操作
            Runnable reply = null;
            switch (mqttQoS) {
                case AT_MOST_ONCE:
                    if (packetId != 0) {
                        // todo disconnect
                    }
                    reply = () -> {
                    };
                    break;
                case AT_LEAST_ONCE:

                    reply = () -> {
                        //qos 1 ,返回ack
                        log.debug("begin send pubAck to the client,key :{}", clientInfo.key());
                        downStreamHandler.replyPubAck(clientInfo.clientId(), packetId);

                    };
                    break;
                case EXACTLY_ONCE:
                    if (mqttConfig.isEnableQos2()) {
                        reply = () -> {
                            log.debug("begin send pubAck to the client,key :{}", clientInfo.key());
                            downStreamHandler.replyPubRec(clientInfo.clientId(), packetId);
                        };
                    } else {
                        // 埋点
                        log.info("clientId:{} will be disconnected, the mqtt server doesn't support QoS 2, channel:{}", clientId, channel);
                        connectStatusHandler.disconnect(channel, "server does not support qos 2");
                        return;
                    }
                    break;
                case FAILURE:
                    log.error("clientId:{} will be disconnected, publish qos error, channel:{}", clientId, channel);
                    connectStatusHandler.disconnect(channel, "server not support qos " + mqttQoS);
                    return;
            }


            // 做事件通知，通知完成后做响应

            CompletableFuture<?> extendTask = null;
            if (extendProcessor != null) {
                // 对收到消息进行流转平台,可以通过协议的retain处理，此处全部保留
                extendTask = extendProcessor.onReceivePublishMessage(mqttQoS, topicName, payload, clientInfo, timeMillis, dup, retain);
            }
            if (extendTask != null) {
                // 先存储
                extendTask.thenRun(reply).exceptionally((e) -> {
                    log.error("process publish extend operation error", e);
                    //publish消息时出现系统错误，不返回数据
                    return null;
                });
            } else {
                reply.run();
            }
        });


    }


    @Override
    public void handleDisconnect(Channel channel, MqttMessage msg) {
        MqttFixedHeader fixedHeader = msg.fixedHeader();
        MqttMessageType messageType = fixedHeader.messageType();

        MqttReasonCodeAndPropertiesVariableHeader variableHeader = (MqttReasonCodeAndPropertiesVariableHeader) msg.variableHeader();
        byte reasonCode = variableHeader.reasonCode();
        MqttProperties properties = variableHeader.properties();

    }

    @Override
    public void handleAuth(Channel channel, MqttMessage msg) {
        MqttFixedHeader fixedHeader = msg.fixedHeader();
        MqttMessageType messageType = fixedHeader.messageType();

        MqttReasonCodeAndPropertiesVariableHeader variableHeader = (MqttReasonCodeAndPropertiesVariableHeader) msg.variableHeader();
    }

    @Override
    public void handlePubAck(Channel channel, MqttPubAckMessage msg) {
        MqttFixedHeader fixedHeader = msg.fixedHeader();
        MqttMessageType messageType = fixedHeader.messageType();

        MqttPubReplyMessageVariableHeader variableHeader = (MqttPubReplyMessageVariableHeader) msg.variableHeader();
        int messageId = variableHeader.messageId();
        // 当原因码为0x00（成功）且没有属性（Properties）时，原因码和属性长度可以被省略,
        //new MqttPubReplyMessageVariableHeader(packetId, (byte)0, MqttProperties.NO_PROPERTIES)
        byte reasonCode = variableHeader.reasonCode();
        MqttProperties properties = variableHeader.properties();


    }

    @Override
    public void handlePingReq(Channel channel, MqttMessage msg) {
        String clientId = NettyUtils.clientId(channel);
        if (clientId == null) {
            log.info("server not find clientId in channel={}", channel);
            connectStatusHandler.disconnect(channel, "server not find clientId in channel");
            return;
        }
        downStreamHandler.replyPingResp(channel);
    }


    @Override
    public void handleUnsubscribe(Channel channel, MqttUnsubscribeMessage msg) {
        String clientId = NettyUtils.clientId(channel);
        if (!mqttConfig.isEnableSubscribe()) {
            log.info("this broker dons't enable subscribe, the connect will disconnect,clientId={},chan={}", clientId, channel);

            connectStatusHandler.disconnect(channel, "not support subscribe");
            return;
        }
        ConnectionDescriptor conn = this.connectRelation.getConnByClientId(clientId);
        if (conn == null) {
            connectStatusHandler.disconnect(channel, "connection relation is not found");
        }
        MqttClientInfo clientInfo = conn.clientInfo();
        boolean result = processExecutorGroup.submitProcessTask(clientId, () -> {
            MqttFixedHeader fixedHeader = msg.fixedHeader();
            MqttMessageType messageType = fixedHeader.messageType();
            // 1
            MqttQoS mqttQoS = fixedHeader.qosLevel();
            // false
            boolean dup = fixedHeader.isDup();
            boolean retain = fixedHeader.isRetain();


            MqttMessageIdAndPropertiesVariableHeader variableHeader = (MqttMessageIdAndPropertiesVariableHeader) msg.variableHeader();
            int messageId = variableHeader.messageId();
            MqttProperties properties = variableHeader.properties();

            MqttUnsubscribePayload payload = msg.payload();
            List<String> topics = payload.topics();
            if (topics.isEmpty()) {
                connectStatusHandler.disconnect(channel, "unsubscription topics is empty");
            }
            // topics 校验和取消操作
            List<Short> unsubResult = new LinkedList<>();
            // 回复ack
            downStreamHandler.replyUnsubAck(channel, messageId, unsubResult);

        });

    }

    @Override
    public void handleSubscribe(Channel channel, MqttSubscribeMessage msg) {

        String clientId = NettyUtils.clientId(channel);
        if (!mqttConfig.isEnableSubscribe()) {
            log.info("this broker dons't enable subscribe, the connect will disconnect,clientId={},chan={}", clientId, channel);

            connectStatusHandler.disconnect(channel, "not support subscribe");
            return;
        }
        ConnectionDescriptor conn = this.connectRelation.getConnByClientId(clientId);
        if (conn == null) {
            connectStatusHandler.disconnect(channel, "connection relation is not found");
        }
        MqttClientInfo clientInfo = conn.clientInfo();

        boolean result = processExecutorGroup.submitProcessTask(clientId, () -> {
            MqttFixedHeader fixedHeader = msg.fixedHeader();
            MqttMessageType messageType = fixedHeader.messageType();
            // 1
            MqttQoS mqttQoS = fixedHeader.qosLevel();
            // false
            boolean dup = fixedHeader.isDup();
            boolean retain = fixedHeader.isRetain();


            MqttMessageIdAndPropertiesVariableHeader variableHeader = (MqttMessageIdAndPropertiesVariableHeader) msg.variableHeader();
            int messageId = variableHeader.messageId();
            MqttProperties properties = variableHeader.properties();

            MqttSubscribePayload payload = msg.payload();
            List<MqttTopicSubscription> topicSubscriptions = payload.topicSubscriptions();
            if (topicSubscriptions.isEmpty()) {
                connectStatusHandler.disconnect(channel, "subscription filters is empty");
            }
            // 此处可将报文需要部分直接事件通知，又上层做业务实现异步处理（方便升级处理时不需要考虑网络断开问题）


            List<Integer> subResult = new LinkedList<>();
            topicSubscriptions.forEach(subscription -> {
                // fliter,qos,option
                // $share/{ShareName}/{filter} 表示共享订阅
                String topicName = subscription.topicName();
                MqttQoS qoS = subscription.qualityOfService();
                MqttSubscriptionOption option = subscription.option();
                // 是否发送到自身
                boolean noLocal = option.isNoLocal();
                // 保留标志是否为publish时的
                boolean retainAsPublished = option.isRetainAsPublished();
                // 表示最大消息质量
                MqttQoS optionQos = option.qos();
                // 订阅建立时，是否发送保留消息
                MqttSubscriptionOption.RetainedHandlingPolicy retainedHandlingPolicy = option.retainHandling();

                // 开始校验和


            });


            downStreamHandler.replySubAck(channel, messageId, subResult);
        });
        if (!result) {
            log.error("sys error , submitProcessTask fail .clientId={},chan={}", clientId, channel);
        }
    }

    @Override
    public void handlePubRel(Channel channel, MqttMessage msg) {

    }

    @Override
    public void handlePubRec(Channel channel, MqttMessage msg) {

    }

    @Override
    public void handlePubComp(Channel channel, MqttMessage msg) {

    }


}
