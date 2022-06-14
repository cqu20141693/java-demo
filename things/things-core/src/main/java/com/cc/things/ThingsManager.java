package com.cc.things;

import com.cc.things.deivce.DeviceOperator;

/**
 * 物管理器
 * wcc 2022/6/4
 */
public interface ThingsManager {
   DeviceOperator getDevice(String deviceId);
}
