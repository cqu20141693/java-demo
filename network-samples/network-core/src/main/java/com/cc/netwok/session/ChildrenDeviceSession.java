package com.cc.netwok.session;

import com.cc.netwok.gateway.Transport;
import com.cc.netwok.message.EncodedMessage;
import com.cc.things.deivce.DeviceOperator;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 子设备会话
 * wcc 2022/6/29
 */
@Data
@Slf4j
public class ChildrenDeviceSession implements DeviceSession {
    @Getter
    private final String id;

    @Getter
    private final String deviceId;

    private final DeviceSession parent;

    @Getter
    private final DeviceOperator operator;

    private List<Runnable> closeListener;

    private long lastKeepAliveTime;

    private long keepAliveTimeOutMs = -1;

    public ChildrenDeviceSession(String deviceId, DeviceSession parent, DeviceOperator operator) {
        this.id = deviceId;
        this.parent = parent;
        this.operator = operator;
        this.deviceId = deviceId;
        this.lastKeepAliveTime = parent.lastPingTime();

    }

    public DeviceOperator getParentDevice() {
        return parent.getOperator();
    }

    @Override
    public long lastPingTime() {
        return lastKeepAliveTime;
    }

    @Override
    public long connectTime() {
        return parent.connectTime();
    }

    @Override
    public Boolean send(EncodedMessage encodedMessage) {
        log.info("send child device[{}:{}] message", parent.getDeviceId(), deviceId);
        return parent.send(encodedMessage);
    }

    @Override
    public Transport getTransport() {
        return parent.getTransport();
    }

    @Override
    public void close() {
        if (null != closeListener) {
            closeListener.forEach(Runnable::run);
        }
    }

    @Override
    public void ping() {
        parent.ping();
        this.lastKeepAliveTime = System.currentTimeMillis();
    }

    @Override
    public boolean isAlive() {
        if (keepAliveTimeOutMs <= 0) {
            return parent.isAlive();
        }
        return System.currentTimeMillis() - lastKeepAliveTime < keepAliveTimeOutMs
                && parent.isAlive();
    }

    @Override
    public synchronized void onClose(Runnable call) {
        if (closeListener == null) {
            closeListener = new CopyOnWriteArrayList<>();
        }
        closeListener.add(call);
    }

    @Override
    public Optional<String> getServerId() {
        return parent.getServerId();
    }

    @Override
    public boolean isWrapFrom(Class<?> type) {
        return type == ChildrenDeviceSession.class || parent.isWrapFrom(type);
    }

    @Override
    public Optional<InetSocketAddress> getClientAddress() {
        return parent.getClientAddress();
    }

    @Override
    public void setKeepAliveTimeout(Duration timeout) {
        keepAliveTimeOutMs = timeout.toMillis();
    }

    @Override
    public Duration getKeepAliveTimeout() {
        return Duration.ofMillis(keepAliveTimeOutMs);
    }

    @Override
    public <T extends DeviceSession> T unwrap(Class<T> type) {
        return type == ChildrenDeviceSession.class ? type.cast(this) : parent.unwrap(type);
    }

    @Override
    public String toString() {
        return "children device[" + deviceId + "] in " + parent;
    }
}
