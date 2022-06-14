package com.cc.gb28181.media;

import com.cc.gb28181.entity.MediaGatewayEntity;

/**
 * 视频设备接入网关提供商,不同的视频设备接入方式有不同的实现
 */
public interface MediaGatewayProvider {

    /**
     * @return 唯一标识
     */
    String getId();

    /**
     * @return 名称
     */
    String getName();

    /**
     * 根据配置创建网关，创建的网关不会自动启动，需要调用{@link MediaGateway#start()}进行启动
     *
     * @param config 配置
     * @return 网关
     * @see MediaGateway
     */
    MediaGateway createMediaGateway(MediaGatewayEntity config);
}
