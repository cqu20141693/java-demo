package com.cc.gb28181.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cc.gb28181.GB28181DeviceChannel;
import com.cc.sip.SipDeviceMessage;
import com.cc.things.deivce.DeviceMessage;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.cc.gb28181.XmlUtils.safeString;

@Getter
@Setter
public class CatalogInfo implements SipDeviceMessage {

    @JacksonXmlProperty(localName = "DeviceID")
    private String deviceId;

    @JacksonXmlProperty(localName = "SN")
    private String sn;

    @JacksonXmlProperty(localName = "SumNum")
    private String _sumNum;

    @JacksonXmlProperty(localName = "DeviceList")
    private List<GB28181DeviceChannel> channelList;

    @Override
    public DeviceMessage toDeviceMessage() {
        return null;
    }

    @Override
    public int totalPart() {
        return getSumNum();
    }

    @Override
    public int numberOfPart() {
        return channelList == null ? 0 : channelList.size();
    }

    public int getSumNum() {
        return _sumNum == null ? 0 : Integer.parseInt(_sumNum);
    }

    public void setSumNum(int sumNum) {
        this._sumNum = String.valueOf(sumNum);
    }

    public void setChannelList(List<GB28181DeviceChannel> channelList) {
        this.channelList = channelList;
        if (this.deviceId != null) {
            this.channelList.forEach(channel -> channel.setDeviceId(deviceId));
        }
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        if (this.channelList != null) {
            this.channelList.forEach(channel -> channel.setDeviceId(deviceId));
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.PrettyFormat);
    }


    public String toXml(String charset) {
        StringBuilder body = new StringBuilder("<?xml version=\"1.0\" encoding=\"" + charset + "\"?>\n" +
                "<Response>\n" +
                "<CmdType>Catalog</CmdType>\n" +
                "<SN>" + getSn() + "</SN>\n" +
                "<DeviceID>" + getDeviceId() + "</DeviceID>\n" +
                "<Result>OK</Result>\n" +
                "<SumNum>" + getSumNum() + "</SumNum>\n" +
                "<DeviceList Num=\"" + getChannelList().size() + "\">");

        for (GB28181DeviceChannel channel : getChannelList()) {

            body
                    .append("<Item>\n")
                    .append("<DeviceID>").append(channel.getChannelId()).append("</DeviceID>\n")
                    .append("<Name>").append(safeString(channel.getName())).append("</Name>\n")
                    .append("<Manufacturer>").append(safeString(channel.getManufacturer())).append("</Manufacturer>\n")
                    .append("<Model>").append(safeString(channel.getModel())).append("</Model>\n")
                    .append("<Owner>").append(safeString(channel.getOwner())).append("</Owner>\n")
                    .append("<CivilCode>").append(safeString(channel.getCivilCode())).append("</CivilCode>\n")
                    .append("<Block>").append(safeString(channel.getBlock())).append("</Block>\n")
                    .append("<Address>").append(safeString(channel.getAddress())).append("</Address>\n")
                    .append("<Parental>").append(safeString(channel.getParental())).append("</Parental>\n")
                    .append("<ParentID>").append(safeString(channel.getParentId())).append("</ParentID>\n")
                    .append("<SafetyWay>").append(safeString(channel.getSafetyWay())).append("</SafetyWay>\n")
                    .append("<RegisterWay>").append(safeString(channel.getRegisterWay())).append("</RegisterWay>\n")
                    .append("<CertNum>").append(safeString(channel.getCertNum())).append("</CertNum>\n")
                    .append("<Certifiable>").append(safeString(channel.getCertifiable())).append("</Certifiable>\n")
                    .append("<ErrCode>").append(safeString(channel.getErrCode())).append("</ErrCode>\n")
                    .append("<EndTime>").append(safeString(channel.getEndTime())).append("</EndTime>\n")
                    .append("<Secrecy>").append(safeString(channel.getSecrecy())).append("</Secrecy>\n")
                    .append("<IPAddress>").append(safeString(channel.getIpAddress())).append("</IPAddress>\n")
                    .append("<Port>").append(safeString(channel.getPort())).append("</Port>\n")
                    .append("<Password>").append(safeString(channel.getPassword())).append("</Password>\n")
                    .append("<Status>").append(safeString(channel.getStatus().getCode())).append("</Status>\n")
                    .append("<Longitude>").append(safeString(channel.getLongitude())).append("</Longitude>\n")
                    .append("<Latitude>").append(safeString(channel.getLatitude())).append("</Latitude>\n");
            if (channel.getInfo() != null) {
                body.append("<Info>")
                        .append(channel.getInfo().toXML())
                        .append("</Info>\n");
            }

            body.append("</Item>\n");
        }
        body.append("</DeviceList>\n</Response>");
        return body.toString();
    }
}
