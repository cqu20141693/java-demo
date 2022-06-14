package com.cc.gb28181.media.server.zlm;

import com.alibaba.fastjson.JSONObject;
import com.cc.gb28181.media.server.StreamInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Optional;

@Getter
@Setter
public class DeviceStreamInfo extends StreamInfo {
    private String deviceId;

    private String channelId;

    private String serverId;

    private String gatewayId;

    private Map<String, Object> others;

    public StreamInfo toStreamInfo() {
        String jsonString = JSONObject.toJSONString(this);
        return JSONObject.parseObject(jsonString, StreamInfo.class);
    }

    public Optional<Object> other(String key) {
        if (others == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(others.get(key));
    }
}
