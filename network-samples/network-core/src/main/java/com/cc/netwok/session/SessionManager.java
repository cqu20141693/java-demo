package com.cc.netwok.session;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 会话管理器，用于管理所有的设备连接会话
 * wcc 2022/6/28
 */
public interface SessionManager {
    /**
     * 根据设备ID或者会话ID获取设备会话
     *
     * @param idOrDeviceId 设备ID或者会话ID
     * @return 设备会话, 不存在则返回<code>null</code>
     */
    @Nullable
    DeviceSession getSession(String idOrDeviceId);

    /**
     * 注册新到设备会话,如果已经存在相同设备ID到会话,将注销旧的会话.
     *
     * @param session 新的设备会话
     * @return 旧的设备会话, 不存在则返回<code>null</code>
     */
    @Nullable
    DeviceSession register(DeviceSession session);

    /**
     * 替换session
     *
     * @param oldSession 旧session
     * @param newSession 新session
     * @return 新session
     * @since 1.1.1
     */
    default DeviceSession replace(DeviceSession oldSession, DeviceSession newSession) {
        return newSession;
    }

    /**
     * 使用会话ID或者设备ID注销设备会话
     *
     * @param idOrDeviceId 设备ID或者会话ID
     * @return 被注销的会话, 不存在则返回<code>null</code>
     */
    DeviceSession unregister(String idOrDeviceId);

    boolean sessionIsAlive(String deviceId);

    @Nullable
    ChildrenDeviceSession getSession(String deviceId, String childrenId);

    ChildrenDeviceSession registerChildren(String deviceId, String childrenDeviceId);

    ChildrenDeviceSession unRegisterChildren(String deviceId, String childrenId);

    List<DeviceSession> onRegister();

    List<DeviceSession> onUnRegister();

    List<DeviceSession> getAllSession();

}
