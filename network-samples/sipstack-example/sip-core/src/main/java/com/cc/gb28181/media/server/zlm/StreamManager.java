package com.cc.gb28181.media.server.zlm;

/**
 * wcc 2022/6/6
 * 流管理器
 */
public interface StreamManager {
    DeviceStreamInfo findStreamByStreamId(String deviceId, String streamId);
}
