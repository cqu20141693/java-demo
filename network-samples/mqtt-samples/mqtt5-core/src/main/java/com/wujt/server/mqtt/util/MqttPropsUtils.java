package com.wujt.server.mqtt.util;

import io.netty.handler.codec.mqtt.MqttProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wujt
 */
public class MqttPropsUtils {

    // connect

    /**
     * 17 (0x11) 会话过期间隔（Session Expiry Interval）标识符
     */
    public static int getSessionExpiryInterval(MqttProperties properties) {
        return (int) properties.getProperties(17).get(0).value();
    }

    /**
     * todo
     * 33 (0x21) 接收最大值（Receive Maximum）标识符
     * 接收最大值只将被应用在当前网络连接。如果没有设置最大接收值，将使用默认值65535
     */
    public static Integer getReceiveMax(MqttProperties properties) {
        return (Integer) properties.getProperties(32).get(0).value();
    }

    /**
     * todo
     * 39 (0x27)，最大报文长度（Maximum Packet Size）标识符。
     *
     * @param properties
     * @return
     */
    public static Integer getMaxPacketSize(MqttProperties properties) {
        return (Integer) properties.getProperties(39).get(0).value();
    }

    /**
     * todo
     * 34 (0x22)，主题别名最大值（Topic Alias Maximum）标识符。
     * 发送端指定一次连接的最大值 4.10
     *
     * @param properties
     * @return
     */
    public static Integer getTopicAliasMax(MqttProperties properties) {
        return (Integer) properties.getProperties(34).get(0).value();
    }

    /**
     * todo
     * 25 (0x19)，请求响应信息（Request Response Information）标识符。
     * 一个字节表示的0或1
     *
     * @param properties
     * @return
     */
    public static Integer getReqRespInfo(MqttProperties properties) {
        return (Integer) properties.getProperties(25).get(0).value();
    }

    /**
     * todo
     * 23 (0x17)，请求问题信息（Request Problem Information）标识符。
     *
     * @param properties
     * @return
     */
    public static Integer getReqProblemInfo(MqttProperties properties) {
        return (Integer) properties.getProperties(23).get(0).value();
    }

    /**
     * 38 (0x26)，用户属性（User Property）标识符。
     * UTF-8字符串对
     *
     * @param properties
     * @return
     */
    public static HashMap<String, String> getUserProperty(MqttProperties properties) {
        HashMap<String, String> map = new HashMap<>();
        List<MqttProperties.UserProperty> userProperties = (List<MqttProperties.UserProperty>) properties.getProperties(38);
        userProperties.forEach((userProperty) -> {
            map.put(userProperty.value().key, userProperty.value().value);
        });

        return map;
    }

    /**
     * todo
     * 21 (0x15)，认证方法（Authentication Method）标识符。
     *
     * @param properties
     * @return
     */
    public static String getAuthenticationMethod(MqttProperties properties) {
        return (String) properties.getProperties(21).get(0).value();
    }

    /**
     * todo
     * 22 (0x16)，认证数据（Authentication Data）标识符。
     *
     * @param properties
     * @return
     */
    public static byte[] getAuthenticationData(MqttProperties properties) {
        return (byte[]) properties.getProperties(22).get(0).value();
    }

    // will

    /**
     * 24 (0x18)，遗嘱延时间隔（Will Delay Interval）标识符。
     *
     * @param properties
     * @return
     */
    public static Integer getWillDelayInterval(MqttProperties properties) {
        return (Integer) properties.getProperties(24).get(0).value();
    }

    /**
     * 1 (0x01)，载荷格式指示（Payload Format Indicator）标识符。
     * 单字节的载荷格式指示值
     *
     * @param properties
     * @return
     */
    public static Byte getPayloadFormatIndicator(MqttProperties properties) {
        return (Byte) properties.getProperties(1).get(0).value();
    }

    /**
     * 2 (0x02)，消息过期间隔（Message Expiry Interval）标识符。
     * 四字节整数表示的消息过期间隔
     *
     * @param properties
     * @return
     */
    public static Integer getMessageExpiryInterval(MqttProperties properties) {
        return (Integer) properties.getProperties(2).get(0).value();
    }

    /**
     * 3 (0x03)，内容类型（Content Type）标识符。
     * 一个以UTF-8格式编码的字符串
     * 发送程序和接收程序负责内容类型字符串的定义和解释
     *
     * @param properties
     * @return
     */
    public static String getContentType(MqttProperties properties) {
        return (String) properties.getProperties(3).get(0).value();
    }

    /**
     * 8 (0x08)，响应主题（Response Topic）标识符
     * 一个UTF-8编码的字符串,不能包含通配符
     *
     * @param properties
     * @return
     */
    public static String getResponseTopic(MqttProperties properties) {
        return (String) properties.getProperties(8).get(0).value();
    }

    /**
     * 9 (0x09)，对比数据（Correlation Data）标识符。
     * 二进制数据,对比数据被请求消息发送端在收到响应消息时用来标识相应的请求
     *
     * @param properties
     * @return
     */
    public static byte[] getCorrelationData(MqttProperties properties) {
        return (byte[]) properties.getProperties(9).get(0).value();
    }

    // user property


    // connectAck

    /**
     * 17 (0x11)，会话过期间隔（Session Expiry Interval）标识符
     * 默认客户端，否则通知客户端服务端不同的值
     *
     * @param properties
     * @param interval
     */
    public static void setSessionExpiryInterval(MqttProperties properties, Integer interval) {
        MqttProperties.IntegerProperty expiryInterval = new MqttProperties.IntegerProperty(17, interval);
        properties.add(expiryInterval);
    }

    /**
     * 33 (0x21)，接收最大值（Receive Maximum）描述符
     * 服务端使用此值限制服务端愿意为该客户端同时处理的QoS为1和QoS为2的发布消息最大数量
     * 双字节整数表示的最大接收值;没有设置最大接收值，将使用默认值65535
     *
     * @param properties
     * @param maxSize
     */
    public static void setReceiveMax(MqttProperties properties, Short maxSize) {
        ShortProperty receviceMax = new ShortProperty(33, maxSize);
        properties.add(receviceMax);
    }

    /**
     * 36 (0x24)，最大服务质量（Maximum QoS）标识符
     * Receive Maximum 服务端使用此值限制服务端愿意为该客户端同时处理的QoS为1和QoS为2的发布消息最大数量
     * 用一个字节表示的0或1
     *
     * @param properties
     * @param qos
     */
    public static void setMaxQos(MqttProperties properties, Byte qos) {
        ByteProperty maxQos = new ByteProperty(36, qos);
        properties.add(maxQos);
    }

    /**
     * 37 (0x25)，保留可用（Retain Available）标识符
     * 值为0表示不支持保留消息，为1表示支持保留消息。如果没有设置保留可用字段，表示支持保留消息
     * 一个单字节字段
     *
     * @param properties
     * @param available
     */
    public static void setRetainAvailable(MqttProperties properties, Byte available) {

        ByteProperty property = new ByteProperty(37, available);
        properties.add(property);
    }

    /**
     * 39 (0x27)，最大报文长度（Maximum Packet Size）标识符
     * 服务端使用最大报文长度通知客户端其所能处理的单个报文长度限制
     * 四字节整数表示的服务端愿意接收的最大报文长度
     *
     * @param properties
     * @param maxSize
     */
    public static void setMaxPacketSize(MqttProperties properties, Integer maxSize) {

        MqttProperties.IntegerProperty property = new MqttProperties.IntegerProperty(39, maxSize);
        properties.add(property);
    }

    /**
     * 18 (0x12)，分配客户标识符（Assigned Client Identifier）标识符
     * 服务端分配客户标识符的原因是CONNECT报文中的客户标识符长度为0
     *
     * @param properties
     * @param clientIdentifier
     */
    public static void assignClientIndentifier(MqttProperties properties, String clientIdentifier) {

        MqttProperties.StringProperty property = new MqttProperties.StringProperty(18, clientIdentifier);
        properties.add(property);
    }

    /**
     * 34 (0x22)，主题别名最大值（Topic Alias Maximum）标识符
     * 双字节整数表示的主题别名最大值;服务端使用此值来限制本次连接可以拥有的主题别名的值
     * 主题别名最大值（Topic Alias）没有设置，或者设置为0，则客户端不能向此服务端发送任何主题别名
     *
     * @param properties
     * @param aliasMax
     */
    public static void setTopicAliasMax(MqttProperties properties, Short aliasMax) {

        ShortProperty property = new ShortProperty(34, aliasMax);
        properties.add(property);
    }

    /**
     * 31 (0x1F)，原因字符串（Reason String）标识符
     * 服务端使用此值向客户端提供附加信息
     *
     * @param properties
     * @param reason
     */
    public static void setReason(MqttProperties properties, String reason) {

        MqttProperties.StringProperty property = new MqttProperties.StringProperty(31, reason);
        properties.add(property);
    }

    /**
     * 38 (0x26)，用户属性（User Property）标识符
     * UTF-8字符串对
     *
     * @param properties
     * @param userProperty
     */
    public static void setUserProperty(MqttProperties properties, Map<String, String> userProperty) {

        MqttProperties.UserProperties userProperties = new MqttProperties.UserProperties();
        userProperty.forEach(userProperties::add);
        properties.add(userProperties);
    }

    /**
     * 40 (0x28)，通配符订阅可用（Wildcard Subscription Available）标识符
     * 一个单字节字段,值为0表示不支持通配符订阅，值为1表示支持通配符订阅
     *
     * @param properties
     * @param available
     */
    public static void setWildcardSubAvailable(MqttProperties properties, Byte available) {

        ByteProperty property = new ByteProperty(40, available);
        properties.add(property);
    }

    /**
     * 41 (0x29)，订阅标识符可用（Subscription Identifier Available）标识符
     * 一个单字节字段:值为0表示不支持订阅标识符，值为1表示支持订阅标识符
     *
     * @param properties
     * @param available
     */
    public static void setSubIdentifierAvailable(MqttProperties properties, Byte available) {

        ByteProperty property = new ByteProperty(41, available);
        properties.add(property);
    }

    /**
     * 42 (0x2A)，共享订阅可用（Shared Subscription Available）标识符
     * 一个单字节字段,值为0表示不支持共享订阅，值为1表示支持共享订阅
     *
     * @param properties
     * @param available
     */
    public static void setSharedSubAvailable(MqttProperties properties, Byte available) {

        ByteProperty property = new ByteProperty(42, available);
        properties.add(property);
    }

    /**
     * 19 (0x13)，服务端保持连接（Server Keep Alive）标识符
     * 双字节整数表示的保持连接（Keep Alive）时间,
     * 服务端发送了服务端保持连接（Server Keep Alive）属性，客户端必须使用此值代替其在CONNECT报文中发送的保持连接时间值
     *
     * @param properties
     * @param keepLive
     */
    public static void setServerKeepAlive(MqttProperties properties, Short keepLive) {

        ShortProperty property = new ShortProperty(19, keepLive);
        properties.add(property);
    }

    /**
     * 26 (0x1A)，响应信息（Response Information）标识符
     * 一个以UTF-8编码的字符串,作为创建响应主题（Response Topic）的基本信息
     *
     * @param properties
     * @param responseInfo
     */
    public static void setResponseInfo(MqttProperties properties, String responseInfo) {

        MqttProperties.StringProperty property = new MqttProperties.StringProperty(26, responseInfo);
        properties.add(property);
    }

    /**
     * 28 (0x1C)，服务端参考（Server Reference）标识符
     * 一个UTF-8编码的字符串，可以被客户端用来标识其他可用的服务端
     *
     * @param properties
     * @param serverRefrence
     */
    public static void setServerRefrence(MqttProperties properties, String serverRefrence) {

        MqttProperties.StringProperty property = new MqttProperties.StringProperty(28, serverRefrence);
        properties.add(property);
    }

    /**
     * 21 (0x15)，认证方法（Authentication Method）标识符
     * 一个以UTF-8编码的字符串，包含了认证方法
     *
     * @param properties
     * @param authenticationMethod
     */
    public static void setAuthenticationMethod(MqttProperties properties, String authenticationMethod) {

        MqttProperties.StringProperty property = new MqttProperties.StringProperty(21, authenticationMethod);
        properties.add(property);
    }

    /**
     * 22 (0x16)，认证数据（Authentication Data）标识符
     * 认证数据（Authentication Data）的二进制数据。
     *
     * @param properties
     * @param authenticationData
     */
    public static void setAuthenticationData(MqttProperties properties, byte[] authenticationData) {

        MqttProperties.BinaryProperty property = new MqttProperties.BinaryProperty(22, authenticationData);
        properties.add(property);
    }


    //publish

    // 1 (0x01)，载荷格式指示（Payload Format Indicator）标识符
    //2 (0x02)，消息过期间隔（Message Expiry Interval）标识符;服务端发送给客户端的PUBLISH报文中必须包含消息过期间隔，值为接收时间减去消息在服务端的等待时间
    // 35 (0x23)，主题别名（Topic Alias）标识符
    //8 (0x08)，响应主题（Response Topic）标识符
    //9 (0x09)，对比数据（Correlation Data）标识符
    //38 (0x26)，用户属性（User Property）
    //3 (0x03)， 内容类型（Content Type）标识符

    /**
     * todo 5.0 新增
     * 11 (0x0B)，订阅标识符（Subscription Identifier）标识符
     * 一个变长字节整数表示的订阅标识符
     *
     * @param properties
     * @return
     */
    public static Integer getSubIdentifier(MqttProperties properties) {
        return (Integer) properties.getProperties(11).get(0).value();
    }

    /**
     * todo
     * 35 (0x23)，主题别名（Topic Alias）标识符
     * 发送端指定一次连接（生命周期）的topic alias,可修改
     * 表示主题别名（Topic Alias）值的双字节整数
     *
     * @param properties
     * @return
     */
    public static Short getTopicAlias(MqttProperties properties) {
        return (Short) properties.getProperties(35).get(0).value();
    }

    // pubAck

    //31 (0x1F) ，原因字符串（Reason String）标识符
    //38 (0x26) ，用户属性（User Property）标识符


    //disconnect
    // 17 (0x11) ，会话过期间隔（Session Expiry Interval）标识符
    //31 (0x1F) ，原因字符串（Reason String）标识符
    //38 (0x26) ，用户属性（User Property）标识符
    //28 (0x1C) ，服务端参考（Server Reference）标识符


    // subscription

    //11 (0x0B) ，订阅标识符（Subscription Identifier）标识符,一个变长字节整数表示订阅标识符
    //38 (0x26) ，用户属性（User Property）标识符


    // subAck

    //31 (0x1F) ，原因字符串（Reason String）标识符
    //38 (0x26) ，用户属性（User Property）标识符

    // UNSUBSCRIBE
    //38 (0x26) ，用户属性（User Property）标识符
}
