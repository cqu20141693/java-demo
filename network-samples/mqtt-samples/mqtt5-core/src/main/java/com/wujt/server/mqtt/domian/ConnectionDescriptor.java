package com.wujt.server.mqtt.domian;


import com.wujt.server.mqtt.domain.client.MqttClientInfo;
import io.netty.channel.Channel;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wujt
 */
public class ConnectionDescriptor {
    // packetId
    private final AtomicInteger nextMessageId;
    private final Integer MAX_ID;
    // 通道
    private final Channel channel;
    // 服务端侧客户端信息
    private MqttClientInfo clientInfo;

    public ConnectionDescriptor(Channel session) {
        this(session, 0xffff);
    }

    public ConnectionDescriptor(Channel session, Integer maxId) {
        assert 0xffff >= maxId : "maxId must be less than 0xffff";
        this.channel = session;
        MAX_ID = maxId;
        nextMessageId = new AtomicInteger(new Random(maxId).nextInt(maxId));
    }

    public Channel channel() {
        return channel;
    }

    public MqttClientInfo clientInfo() {
        return clientInfo;
    }

    public String sessionKey() {
        return clientInfo.sessionKey();
    }

    public void setClientInfo(MqttClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public int nextPacketId() {
        this.nextMessageId.compareAndSet(MAX_ID, 1);
        return this.nextMessageId.getAndIncrement();
    }
}
