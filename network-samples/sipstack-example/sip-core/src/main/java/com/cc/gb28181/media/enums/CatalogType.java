package com.cc.gb28181.media.enums;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * wcc 2022/5/25
 */
public enum CatalogType {

    /* 18位编码 */
    //数字视音频设备类型码为00~19
    dv_no_storage("数字视频编码设备(不带本地存储)", 0),
    dv_has_storage("数字视频编码设备(带本地存储)", 1),
    dv_decoder("数字视频解码设备", 2),

    //服务器设备类型码为20~39
    networking_monitor_server("监控联网管理服务器", 20),
    media_proxy("视频代理服务器", 21),
    web_access_server("Web接入服务器", 22),
    video_management_server("录像管理服务器", 23),

    //其他数字设备类型码为40~59
    network_matrix("网络数字矩阵", 40),
    network_controller("网络控制器", 41),
    network_alarm_machine("网络报警主机", 42),

    /* 20位编码 */
    //111~130 表示类型为前端主设备
    dvr("DVR", 111),
    video_server("视频服务器", 112),
    encoder("编码器", 113),
    decoder("解码器", 114),

    video_switching_matrix("视频切换矩阵", 115),
    audio_switching_matrix("音频切换矩阵", 116),
    alarm_controller("报警控制器", 117),
    nvr("网络视频录像机(NVR)", 118),
    //119-130 拓展的前端主设备类型
    hvr("混合硬盘录像机(HVR)", 130),

    //131~199 表示类型为前端外围设备
    camera("摄像机", 131),
    ipc("网络摄像机(IPC)", 132),
    display("显示器", 133),
    alarm_input("报警输入设备", 134),
    alarm_output("报警输出设备", 135),
    audio_input("语音输入设备", 136),
    audio_output("语音输出设备", 137),
    mobile_trans("移动传输设备", 138),
    other_outer("其他外围设备", 139),
    //140~199 扩展的前端外围设备类型

    //200~299 表示类型为平台设备
    center_server("中心信令控制服务器", 200),
    web_server("Web应用服务器", 201),
    media_dispatcher("媒体分发服务器编", 202),
    proxy_server("代理服务器", 203),
    secure_server("安全服务器", 204),
    alarm_server("报警服务器", 205),
    database_server("数据库服务器", 206),
    gis_server("GIS服务器", 207),
    management_server("管理服务器编", 208),
    gateway_server("接入网关", 209),
    media_storage_server("媒体存储服务器", 210),
    signaling_secure_gateway("信令安全路由网关", 211),
    business_group("业务分组", 215),
    virtual_group("虚拟组织", 216),

    //212~214, 217~299 扩展的平台设备类型

    //300~399 表示类型为中心用户
    center_user("中心用户", 300),
    //301~343 行业角色用户
    //344~399 扩展的中心用户类型

    //400~499 表 示 类 型 为终端用户
    end_user("终端用户", 400),

    //500~599 表示类型为平台外接服务器

    media_iap("视频图像信息综合应用平台信令服务器", 500),
    media_ops("视频图像信息运维管理平台信令服务器", 501),

    //600~999 为拓展类型

    //650 本系统拓展的行政区划类型
    district("行政区划", 650) ,

    other("其他", 699),

    ;

    CatalogType(String text, int code) {
        this.text = text;
        this.value = code;
    }

    private final String text;

    public int getValue() {
        return value;
    }

    private final int value;

    public static Map<String, CatalogType> mapping = new HashMap<>();

    static {
        for (CatalogType value : values()) {
            mapping.put(String.valueOf(value.value), value);
        }
    }


    public CatalogTypeGroup getGroup() {
        return null;
    }

    public static String getCatalogTypeCode(String deviceId) {
        if (StringUtils.isEmpty(deviceId) || deviceId.length() < 13) {
            return null;
        }
        return deviceId.substring(10, 13);
    }

    public static CatalogType matchGB28181DeviceId(String deviceId) {
        if (StringUtils.isEmpty(deviceId) || deviceId.length() < 13) {
            return district;
        }

        //20位编码
        if (deviceId.length() == 20) {
            String code = deviceId.substring(10, 13);
            return mapping.getOrDefault(code, other);
        }
        //18位编码
        if (deviceId.length() == 18) {
            String code = deviceId.substring(16, 17);
            return mapping.getOrDefault(String.valueOf(Integer.parseInt(code)), other);
        }
        return other;
    }

}
