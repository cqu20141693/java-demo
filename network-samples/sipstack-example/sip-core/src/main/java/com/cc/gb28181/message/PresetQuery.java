package com.cc.gb28181.message;

import com.cc.sip.SipDeviceMessage;
import com.cc.things.deivce.DeviceMessage;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

@Getter
@Setter
public class PresetQuery implements SipDeviceMessage {

    @JacksonXmlProperty(localName = "DeviceID")
    private String deviceId;

    @JacksonXmlProperty(localName = "SN")
    private String sn;

    @JacksonXmlProperty(localName = "PresetList")
    private List<Preset> presetList;

    public List<Preset> safeGetPreset() {
        if (presetList == null) {
            return Collections.emptyList();
        }
        return presetList;
    }

    public String toXml(int sn, String charset) {
        StringJoiner joiner = new StringJoiner("\r\n");
        joiner.add("<?xml version=\"1.0\" encoding=\"" + charset + "\"?>");
        joiner.add("<Query>");
        joiner.add("<CmdType>PresetQuery</CmdType>");
        joiner.add("<SN>" + sn + "</SN>");
        joiner.add("<DeviceID>" + deviceId + "</DeviceID>");
        joiner.add("<PresetList Num=\"" + (CollectionUtils.isEmpty(presetList) ? 0 : presetList.size()) + "\">");

        if (CollectionUtils.isNotEmpty(presetList)) {
            for (Preset preset : presetList) {
                joiner.add("<Item>");
                joiner.add(preset.toXml());
                joiner.add("</Item>");
            }
        }


        joiner.add("</PresetList>");
        joiner.add("</Query>");

        return joiner.toString();
    }

    @Override
    public DeviceMessage toDeviceMessage() {
        return null;
    }
}
