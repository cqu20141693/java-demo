package com.gow.camera.api.lecheng.model.resp;

/**
 * @author gow
 * @date 2021/8/12
 */
public enum MethodType {

    ACCESS_TOKEN("accessToken"),
    // 设备操作
    DEVICE_ONLINE("deviceOnline"),
    GET_DEVICE_TIME("getDeviceTime"),

    // 设备视频画面配置，水印，翻转
    GET_DEVICE_OSD("queryDeviceOsd"),
    FRAME_REVERSE_STATUS("frameReverseStatus"),
    // 设备直播
    BIND_DEVICE_LIVE("bindDeviceLive"),
    UNBIND_LIVE("unbindLive"),
    LIVE_LIST("liveList"),
    GET_LIVE_STATUS("queryLiveStatus"),
    MODIFY_LIVE_PLAN_STATUS("modifyLivePlanStatus"),
    MODIFY_LIVE_PLAN("modifyLivePlan"),
    BATCH_MODIFY_LIVE_PLAN("batchModifyLivePlan"),
    GET_LIVE_STREAM_INFO("getLiveStreamInfo"),
    ;

    MethodType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private String name;
}
