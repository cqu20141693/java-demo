package com.cc.netwok.gateway;

import com.cc.netwok.DefaultNetworkType;
import com.cc.netwok.NetworkType;

/**
 * 设备网关,用于统一管理设备连接,状态以及消息收发
 * wcc 2022/4/26
 */
public interface DeviceGateway {
    /**
     * @return 网关ID
     */
    String getId();

    /**
     * @return 传输协议
     * @see DefaultTransport
     */
    Transport getTransport();

    /**
     * @return 网络类型
     * @see DefaultNetworkType
     */
    NetworkType getNetworkType();

    /**
     * 订阅来自设备到消息,关闭网关时不会结束流.
     *
     * @return 设备消息流
     */
    Message onMessage();

    /**
     * 启动网关
     *
     * @return 启动结果
     */
    Void startup();

    /**
     * 暂停网关,暂停后停止处理设备消息.
     *
     * @return 暂停结果
     */
    Void pause();

    /**
     * 关闭网关
     *
     * @return 关闭结果
     */
    Void shutdown();

    default boolean isAlive() {
        return true;
    }
}
