package com.cc.netwok.session;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 设备默认会话管理
 * wcc 2022/7/4
 */
@Slf4j
public class DeviceSessionManager implements SessionManager {
    private final Map<String, DeviceSession> repository = new ConcurrentHashMap<>();
    private final Map<String, Map<String, ChildrenDeviceSession>> children = new ConcurrentHashMap<>();

    @Nullable
    @Override
    public DeviceSession getSession(String idOrDeviceId) {
        return repository.get(idOrDeviceId);
    }

    @Nullable
    @Override
    public DeviceSession register(DeviceSession session) {
        // 单机版本可以在内存，集群版本可以放到全局路由表
        DeviceSession old = repository.put(session.getDeviceId(), session);
        if (old != null) {
            log.info("剔除old device link,deviceId={}", old.getDeviceId());
        }
        log.info("send deviceId={} online event", session.getDeviceId());
        return old;
    }

    @Override
    public DeviceSession unregister(String idOrDeviceId) {
        return null;
    }

    @Override
    public boolean sessionIsAlive(String deviceId) {
        return repository.get(deviceId).isAlive();
    }

    @Nullable
    @Override
    public ChildrenDeviceSession getSession(String deviceId, String childrenId) {
        return null;
    }

    @Override
    public ChildrenDeviceSession registerChildren(String deviceId, String childrenDeviceId) {
        return null;
    }

    @Override
    public ChildrenDeviceSession unRegisterChildren(String deviceId, String childrenId) {
        return null;
    }

    @Override
    public List<DeviceSession> onRegister() {
        return null;
    }

    @Override
    public List<DeviceSession> onUnRegister() {
        return null;
    }

    @Override
    public List<DeviceSession> getAllSession() {
        return null;
    }
}
