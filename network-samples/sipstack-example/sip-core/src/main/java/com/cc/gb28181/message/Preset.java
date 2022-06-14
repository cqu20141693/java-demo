package com.cc.gb28181.message;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Preset {
    @JacksonXmlProperty(localName = "PresetID")
    private String id;

    @JacksonXmlProperty(localName = "PresetName")
    private String name;

    public String toXml() {
        return "<PresetID>" + id + "</PresetID>\n" +
                "<PresetName>" + name + "</PresetName>";

    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        return map;
    }
}
