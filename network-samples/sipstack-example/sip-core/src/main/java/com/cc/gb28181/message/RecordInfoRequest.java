package com.cc.gb28181.message;

import com.cc.gb28181.XmlUtils;
import com.cc.sip.SipDeviceMessage;
import com.cc.things.deivce.DeviceMessage;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Getter
@Setter
@EqualsAndHashCode
@Slf4j
public class RecordInfoRequest implements SipDeviceMessage {
    @JacksonXmlProperty(localName = "DeviceID")
    private String deviceId;

    @JacksonXmlProperty(localName = "SN")
    private String sn;

    @JacksonXmlProperty(localName = "StartTime")
    private String startTime;

    @JacksonXmlProperty(localName = "EndTime")
    private String endTime;

    @JacksonXmlProperty(localName = "FilePath")
    private String filePath;

    @JacksonXmlProperty(localName = "Address")
    private String address;

    /**
     * 保密属性(可选)缺省为0;0:不涉密,1:涉密
     */
    @JacksonXmlProperty(localName = "Secrecy")
    private String secrecy;

    /**
     * 录像产生类型(可选)time或alarm 或 manual或all
     */
    @JacksonXmlProperty(localName = "Type")
    private String type = "all";

    /**
     * 录像触发者ID(可选)
     */
    @JacksonXmlProperty(localName = "RecorderID")
    private String recorderId;

    /**
     * 录像模糊查询属性(可选)缺省为0;
     * 0:不进行模糊查询,此时根据 SIP 消息中 To头域 URI中的ID值确定查询录像位置,若ID值为本域系统ID
     * 则进行中心历史记录检索,若为前 端设备ID则进行前端设备历史记录检索;
     * 1:进行模糊查询,此时设备所在域应同时进行中心 检索和前端检索并将结果统一返回。
     */
    @JacksonXmlProperty(localName = "IndistinctQuery")
    private String indistinctQuery = "0";

    public String toXml(int sn, String charset) {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("<?xml version=\"1.0\" encoding=\"" + charset + "\"?>");
        joiner.add("<Query>");
        joiner.add("<CmdType>RecordInfo</CmdType>");
        joiner.add("<SN>" + sn + "</SN>");
        joiner.add("<DeviceID>" + deviceId + "</DeviceID>");
        joiner.add("<StartTime>" + startTime + "</StartTime>");
        joiner.add("<EndTime>" + endTime + "</EndTime>");
        joiner.add("<FilePath>" + XmlUtils.safeString(filePath) + "</FilePath>");
        joiner.add("<Address>" + XmlUtils.safeString(address) + "</Address>");
        joiner.add("<Secrecy>" + XmlUtils.safeString(secrecy) + "</Secrecy>");
        joiner.add("<Type>" + XmlUtils.safeString(type) + "</Type>");
        joiner.add("<RecorderID>" + XmlUtils.safeString(recorderId) + "</RecorderID>");
        joiner.add("</Query>");

        return joiner.toString();
    }

    public List<RecordFile> sendMessage() {
        log.info("invoke record info request message and get record file reply");
        return new ArrayList<>();
    }

    @Override
    public DeviceMessage toDeviceMessage() {
        return null;
    }
}
