package com.cc.gb28181.message;

import com.cc.sip.SipDeviceMessage;
import com.cc.things.deivce.DeviceMessage;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KeepaliveMessage implements SipDeviceMessage {

    @JacksonXmlProperty(localName = "DeviceID")
    private String deviceId;

    @JacksonXmlProperty(localName = "Status")
    private String status;

    @JacksonXmlProperty(localName = "SN")
    private String sn;

    //故障设备列表
    @JacksonXmlProperty(localName = "Info")
    private List<String> info;

    public boolean statusIsOk() {
        return "OK".equals(status);
    }

    @Override
    public DeviceMessage toDeviceMessage() {
        return null;
    }
}
