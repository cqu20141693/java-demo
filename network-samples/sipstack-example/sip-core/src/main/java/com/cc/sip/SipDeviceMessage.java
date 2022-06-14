package com.cc.sip;

import com.cc.things.deivce.DeviceMessage;

/**
 * sip 设备消息
 * wcc 2022/5/14
 */
public interface SipDeviceMessage {
    /**
     * 获取设备ID
     *
     * @return
     */
    String getDeviceId();

    /**
     * 获取设备SN
     *
     * @return
     */
    String getSn();

    /**
     * 转化为设备消息
     *
     * @return
     */
    DeviceMessage toDeviceMessage();

    default int totalPart() {
        return 1;
    }

    default int numberOfPart() {
        return 1;
    }
}
