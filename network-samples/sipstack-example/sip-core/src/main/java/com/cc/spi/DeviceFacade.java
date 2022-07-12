package com.cc.spi;

import com.cc.gb28181.gateway.GB28181Device;

/**
 * 设备服务相关依赖
 * wcc 2022/6/28
 */
public interface DeviceFacade {

    /**
     * 获取视频设备
     *
     * @param deviceId
     * @return
     */
    GB28181Device getDevice(String deviceId);
}
