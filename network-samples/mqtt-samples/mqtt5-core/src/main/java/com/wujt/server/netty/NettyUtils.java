package com.wujt.server.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * @author wujt
 */
public class NettyUtils {

    private static final String ATTR_CLIENT_ID = "clientId";
    //    private static final String ATTR_DEVICE_KEY = "deviceKey";
    private static final String ATTR_MQTT_CLEAN_SESSION = "cleanSession";
    private static final String ATTR_MQTT_WILL_TOPIC = "willTopic";
    private static final String ATTR_MQTT_WILL_MSG = "willMsg";
    private static final String ATTR_MQTT_WILL_QOS = "willQos";
    private static final String ATTR_MQTT_CONNECT_REC = "connectRec";
    private static final String ATTR_MQTT_CONNECT_SEND = "connected";
    private static final String ATTR_MQTT_DISCONNECTED = "disconnected";


    private static final AttributeKey<String> ATTR_KEY_CLIENT_ID = AttributeKey.valueOf(ATTR_CLIENT_ID);
    //    private static final AttributeKey<String> ATTR_KEY_DEVICE_KEY = AttributeKey.valueOf(ATTR_DEVICE_KEY);
    private static final AttributeKey<String> ATTR_KEY_WILL_TOPIC = AttributeKey.valueOf(ATTR_MQTT_WILL_TOPIC);
    private static final AttributeKey<byte[]> ATTR_KEY_WILL_MSG = AttributeKey.valueOf(ATTR_MQTT_WILL_MSG);
    private static final AttributeKey<Integer> ATTR_KEY_WILL_QOS = AttributeKey.valueOf(ATTR_MQTT_WILL_QOS);
    private static final AttributeKey<Boolean> ATTR_KEY_CLEAN_SESSION = AttributeKey.valueOf(ATTR_MQTT_CLEAN_SESSION);
    private static final AttributeKey<Boolean> ATTR_KEY_CONNECT_REC = AttributeKey.valueOf(ATTR_MQTT_CONNECT_REC);
    private static final AttributeKey<Boolean> ATTR_KEY_CONNECT_SEND = AttributeKey.valueOf(ATTR_MQTT_CONNECT_SEND);
    private static final AttributeKey<Boolean> ATTR_KEY_DISCONNECTED = AttributeKey.valueOf(ATTR_MQTT_DISCONNECTED);

    public static void clientId(Channel channel, String clientID) {
        channel.attr(ATTR_KEY_CLIENT_ID).set(clientID);
    }

    public static String clientId(Channel channel) {
        return channel.attr(ATTR_KEY_CLIENT_ID).get();
    }

    public static void cleanSession(Channel channel, boolean cleanSession) {
        channel.attr(ATTR_KEY_CLEAN_SESSION).set(cleanSession);
    }

    public static Boolean cleanSession(Channel channel) {
        return channel.attr(ATTR_KEY_CLEAN_SESSION).get();
    }


    /**
     * 是否已收到过connect消息
     *
     * @param channel
     * @return
     */
    public static Boolean connectRec(Channel channel) {
        return channel.attr(ATTR_KEY_CONNECT_REC).setIfAbsent(true) != null;
    }

    public static Boolean connectSend(Channel channel) {
        return channel.attr(ATTR_KEY_CONNECT_SEND).get();
    }

    public static void connectSend(Channel channel, boolean send) {
        channel.attr(ATTR_KEY_CONNECT_SEND).set(send);
    }

    public static Boolean getDisconnected(Channel channel) {
        return channel.attr(ATTR_KEY_DISCONNECTED).get();
    }

    public static boolean compareAndSetDisconnected(Channel channel, boolean expected, boolean disconnected) {
        return channel.attr(ATTR_KEY_DISCONNECTED).compareAndSet(expected, disconnected);
    }

    public static void initDisconnected(Channel channel, boolean disconnected) {
        channel.attr(ATTR_KEY_DISCONNECTED).set(disconnected);
    }

    public static byte[] readBytesAndRewind(ByteBuf payload) {
        byte[] payloadContent = new byte[payload.readableBytes()];
        int mark = payload.readerIndex();
        payload.readBytes(payloadContent);
        payload.readerIndex(mark);
        return payloadContent;
    }

    public static void willTopic(Channel channel, String willTopic) {
        channel.attr(ATTR_KEY_WILL_TOPIC).set(willTopic);
    }

    public static void willMsg(Channel channel, byte[] willMsg) {
        channel.attr(ATTR_KEY_WILL_MSG).set(willMsg);
    }

    public static void willQos(Channel channel, int willQos) {
        channel.attr(ATTR_KEY_WILL_QOS).set(willQos);
    }
}
