package com.cc.gb28181.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cc.sip.SipDeviceMessage;
import com.cc.things.deivce.DeviceMessage;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.StringJoiner;

/**
 * 配置下载指令
 * wcc 2022/5/24
 */
@Getter
@Setter
public class ConfigDownload implements SipDeviceMessage {

    @JacksonXmlProperty(localName = "DeviceID")
    private String deviceId;

    @JacksonXmlProperty(localName = "SN")
    private String sn;

    @JacksonXmlProperty(localName = "ConfigType")
    private String configType;

    @JacksonXmlProperty(localName = "BasicParam")
    private BasicParam basicParam;

    @JacksonXmlProperty(localName = "VideoParamOp")
    private VideoParamOp videoParamOp;

    @Override
    public DeviceMessage toDeviceMessage() {
        return null;
    }

    public enum ConfigType {
        BasicParam,
        VideoParamOpt,
        SVACEncodeConfig,
        SVACDecodeConfig
    }

    public boolean configTypeIs(ConfigType type) {
        return type.name().equals(configType);
    }

    // TODO: 2021/1/28 更多配置信息


    @Getter
    @Setter
    public static class BasicParam {

        @JacksonXmlProperty(localName = "Name")
        private String name;

        //注册过期时间
        @JacksonXmlProperty(localName = "Expiration")
        private String expiration;

        //心跳间隔时间
        @JacksonXmlProperty(localName = "HeartBeatInterval")
        private int heartBeatInterval;

        //心跳超时次数
        @JacksonXmlProperty(localName = "HeartBeatCount")
        private int heartBeatCount = 5;

        //定位功能支持情况,取值:0-不支持;1-支持 GPS定位;2-支持北斗定位(可选, 默认取值为0)
        @JacksonXmlProperty(localName = "PositionCapability")
        private int positionCapability;

        //经度
        @JacksonXmlProperty(localName = "Longitude")
        private float longitude;

        //纬度
        @JacksonXmlProperty(localName = "Latitude")
        private float latitude;

        public String toXml() {
            StringJoiner joiner = new StringJoiner("\n");
            joiner.add("<Name>" + name + "</Name>");
            joiner.add("<Expiration>" + expiration + "</Expiration>");
            joiner.add("<HeartBeatInterval>" + heartBeatInterval + "</HeartBeatInterval>");
            joiner.add("<HeartBeatCount>" + heartBeatCount + "</HeartBeatCount>");
            joiner.add("<PositionCapability>" + positionCapability + "</PositionCapability>");
            joiner.add("<Longitude>" + longitude + "</Longitude>");
            joiner.add("<Latitude>" + latitude + "</Latitude>");
            return joiner.toString();
        }
    }

    @Getter
    @Setter
    public static class VideoParamOp {

        @JacksonXmlProperty(localName = "DownloadSpeed")
        private String downloadSpeed;

        @JacksonXmlProperty(localName = "Resolution")
        private String resolution;

    }

    public String toXml(int sn, String charset) {
        StringJoiner joiner = new StringJoiner("\r\n");
        joiner.add("<?xml version=\"1.0\" encoding=\"" + charset + "\"?>");
        joiner.add("<Query>");
        joiner.add("<CmdType>ConfigDownload</CmdType>");
        joiner.add("<SN>" + sn + "</SN>");
        joiner.add("<DeviceID>" + deviceId + "</DeviceID>");

        if (configTypeIs(ConfigType.BasicParam) && getBasicParam() != null) {
            joiner.add("<BasicParam>" + getBasicParam().toXml() + "</BasicParam>");
        }
        // TODO: 2021/2/22

        joiner.add("<Info></Info>");

        joiner.add("</Query>");

        return joiner.toString();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.PrettyFormat);
    }
}
