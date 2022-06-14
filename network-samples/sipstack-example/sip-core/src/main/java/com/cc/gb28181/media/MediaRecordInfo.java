package com.cc.gb28181.media;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class MediaRecordInfo {

    private String name;

    private String deviceId;

    private String channelId;

    private String streamId;

    private Long startTime;

    private Long endTime;

    private String filePath;

    private String type;

    private Long fileSize;

    private String recorderId;

    private String secrecy;

    private Map<String, Object> others;

    public Map<String, Object> toMap() {
        return new HashMap<>();
    }
}
