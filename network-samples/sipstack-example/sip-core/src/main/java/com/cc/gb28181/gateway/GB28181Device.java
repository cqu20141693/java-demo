package com.cc.gb28181.gateway;

import com.cc.gb28181.stream.StreamMode;
import com.cc.sip.SipDeviceMessage;
import com.cc.things.deivce.DeviceMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * GB28181 设备
 * wcc 2022/5/24
 */
@Data
public class GB28181Device implements SipDeviceMessage {

    /**
     * 设备Id
     */
    @JsonProperty("DeviceID")
    private String id;

    /**
     * 设备名
     */
    @JsonProperty("DeviceName")
    private String name;

    /**
     * 生产厂商
     */
    @JsonProperty("Manufacturer")
    private String manufacturer;

    /**
     * 型号
     */
    @JsonProperty("Model")
    private String model;

    /**
     * 固件版本
     */
    @JsonProperty("Firmware")
    private String firmware;

    /**
     * 传输协议
     * UDP/TCP
     */
    private String transport;

    /**
     * 数据流传输模式
     */
    private StreamMode streamMode = StreamMode.UDP;

    /**
     * 访问地址
     */
    private String host;

    /**
     * 访问端口
     */
    private int port;

    /**
     * 是否在线
     */
    private boolean online;

    @JsonProperty("Channel")
    private int channelNumber;

    /**
     * 通道列表
     */
    private List<String> channelList;

    @JsonProperty("SN")
    private String sn;

    /**
     * 心跳间隔
     */
    private long heartBeatInterval = 70;

    @Override
    public String getDeviceId() {
        return id;
    }

    @Override
    public DeviceMessage toDeviceMessage() {
        return null;
    }

    public String getHostAndPort() {
        return getHost() + ":" + getPort();
    }

    public void merge(GB28181Device device) {
        if (StringUtils.hasText(device.getHost())) {
            this.setHost(device.getHost());
        }
        if (device.getPort() != 0) {
            this.setPort(device.getPort());
        }

        if(StringUtils.hasText(device.getName())){
            this.setName(device.getName());
        }
        if(StringUtils.hasText(device.getManufacturer())){
            this.setManufacturer(device.getManufacturer());
        }
    }
}
