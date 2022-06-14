package com.cc.gb28181.media;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class DeviceChannel {
    private String id;
    private String deviceId;
    private String name;
    private Map<String,Object> options;
}
