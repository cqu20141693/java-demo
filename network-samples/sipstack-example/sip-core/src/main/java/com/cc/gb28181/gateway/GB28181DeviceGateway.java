package com.cc.gb28181.gateway;

import com.cc.gb28181.media.MediaGateway;
import com.cc.gb28181.media.server.DeviceStreamInfo;
import lombok.Data;

/**
 * wcc 2022/5/25
 */
@Data
public class GB28181DeviceGateway implements MediaGateway {
    @Override
    public String getId() {
        return null;
    }

    @Override
    public Void start() {
        return null;
    }

    @Override
    public Void syncChannel(String id) {
        return null;
    }

    @Override
    public Boolean closeStream(DeviceStreamInfo streamInfo) {
        return null;
    }

    @Override
    public void dispose() {

    }
}
