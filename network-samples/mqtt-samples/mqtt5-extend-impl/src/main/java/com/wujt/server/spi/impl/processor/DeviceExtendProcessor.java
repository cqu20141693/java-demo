package com.wujt.server.spi.impl.processor;


import com.wujt.server.model.ChannelEventInfo;
import com.wujt.server.model.PublishInfo;
import com.wujt.server.mqtt.domain.client.MqttClientInfo;
import com.wujt.server.mqtt.domain.client.MqttDeviceClientInfo;
import com.wujt.server.mqtt.domain.evnet.QosEventInfo;
import com.wujt.server.spi.EventProcessor;
import com.wujt.server.spi.processor.ExtendProcessor;
import com.wujt.server.util.HostUtil;
import io.netty.handler.codec.mqtt.MqttQoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static com.wujt.server.model.ConnEventEnum.*;


/**
 * 设备侧登陆时的EventProcessor
 *
 * @author wujt
 */
@Component
@ConditionalOnMissingBean
public class DeviceExtendProcessor implements ExtendProcessor {

    private static Logger logger = LoggerFactory.getLogger(DeviceExtendProcessor.class);

    @Autowired
    private EventProcessor eventProcessor;

    @Override
    public void onQosEvent(QosEventInfo qosEventInfo) {
        eventProcessor.sendQosEvent(qosEventInfo.getClientId(), qosEventInfo, true);
    }

    @Override
    public CompletableFuture<?> onDeviceConnected(MqttClientInfo clientInfo, long timeStamp) {
        if (clientInfo instanceof MqttDeviceClientInfo) {
            ChannelEventInfo channelEventInfo = createChannelEventInfo((MqttDeviceClientInfo) clientInfo, timeStamp);
            channelEventInfo.setEvent(CONNECT.intValue());
            return eventProcessor.sendChannelEvent(clientInfo.key(), channelEventInfo, true);
        }
        return null;
    }

    @Override
    public void onDeviceDisconnected(MqttClientInfo clientInfo, long timeStamp) {
        if (clientInfo instanceof MqttDeviceClientInfo) {
            ChannelEventInfo channelEventInfo = createChannelEventInfo((MqttDeviceClientInfo) clientInfo, timeStamp);
            channelEventInfo.setEvent(DISCONNECT.intValue());
            eventProcessor.sendChannelEvent(clientInfo.key(), channelEventInfo, false);
        }
    }

    @Override
    public void onDeviceOnline(MqttClientInfo clientInfo, long timeStamp) {
        if (clientInfo instanceof MqttDeviceClientInfo) {
            ChannelEventInfo channelEventInfo = createChannelEventInfo((MqttDeviceClientInfo) clientInfo, timeStamp);
            channelEventInfo.setEvent(ONLINE.intValue());
            eventProcessor.sendChannelEvent(clientInfo.key(), channelEventInfo, false);
        }
    }

    @Override
    public void onChanWritable(MqttClientInfo clientInfo, long timeStamp) {
        if (clientInfo instanceof MqttDeviceClientInfo) {
            ChannelEventInfo channelEventInfo = createChannelEventInfo((MqttDeviceClientInfo) clientInfo, timeStamp);
            channelEventInfo.setEvent(WRITEABLE.intValue());
            eventProcessor.sendChannelEvent(clientInfo.key(), channelEventInfo, true);
        }
    }


    private ChannelEventInfo createChannelEventInfo(MqttDeviceClientInfo deviceClientInfo, long timeStamp) {
        ChannelEventInfo channelEventInfo = new ChannelEventInfo();
        BeanUtils.copyProperties(deviceClientInfo, channelEventInfo);
        channelEventInfo.setTime(timeStamp);
        channelEventInfo.setIp(HostUtil.restIp);
        channelEventInfo.setPort(HostUtil.port);

        // 设置redisClientInfo
        return channelEventInfo;
    }

    @Override
    public CompletableFuture<?> onReceivePublishMessage(MqttQoS qos, String topic, byte[] payload, MqttClientInfo clientInfo, long timeMillis, boolean dup, boolean retain) {
        if (!(clientInfo instanceof MqttDeviceClientInfo)) {
            return null;
        }
        MqttDeviceClientInfo deviceClientInfo = (MqttDeviceClientInfo) clientInfo;
        PublishInfo publishInfo = new PublishInfo();
        BeanUtils.copyProperties(deviceClientInfo, publishInfo);
        // 特有属性设置
        publishInfo.setTopic(topic);
        publishInfo.setPayload(payload);
        publishInfo.setTime(timeMillis);
        publishInfo.setMqttQoS(qos);
        publishInfo.setDup(dup);
        publishInfo.setRetain(retain);
        logger.debug("payload topic= {},payload={} to deviceKey={}", topic, payload, clientInfo.key());
        return eventProcessor.sendPublishEvent(clientInfo.key(), publishInfo, true);
    }

    @Override
    public CompletableFuture<?> onReceivePublishAckMessage(String deviceKey, int deviceId, MqttClientInfo clientInfo, int messageId) {
        return null;
    }

    @Override
    public void onRoutePublishMessage(String clientId, MqttClientInfo clientInfo, int messageId, String topic
            , String filter, int qos, byte[] payload) {

    }

    @Override
    public void onReceiveSubscriptionReq(String clientId, MqttClientInfo clientInfo, String filter, int qos) {

    }

    @Override
    public void onReceiveUnsubscriptionReq(String clientId, MqttClientInfo clientInfo, String filter, int qos) {

    }

}
