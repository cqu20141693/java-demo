package com.cc.gb28181.media;

import com.cc.gb28181.media.server.DeviceStreamInfo;

/**
 * 流媒体接入网关,用于接入流媒体设备
 * wcc 2022/6/4
 */
public interface MediaGateway {

    /**
     * @return ID
     */
    String getId();

    /**
     * 启动网关
     *
     * @return void
     */
    void start();

    /**
     * 同步通道
     *
     * @param id 设备ID
     * @return void
     */
    void syncChannel(String id);

    /**
     * 停止直播流
     *
     * @param streamInfo 直播流信
     * @return void
     */
    Boolean closeStream(DeviceStreamInfo streamInfo);

    /**
     * 停止网关
     */
    void dispose();
}
