package com.cc.gb28181.gateway;

import com.cc.gb28181.entity.MediaGatewayEntity;
import com.cc.gb28181.media.MediaGateway;
import com.cc.gb28181.media.MediaGatewayProvider;
import lombok.Data;

/**
 * wcc 2022/5/25
 */
@Data
public class GB28181MediaGatewayProvider implements MediaGatewayProvider {
    public static final String ID = "gb28181-2016";
    public static final String NAME = "GT28181/2016";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public MediaGateway createMediaGateway(MediaGatewayEntity config) {
        GB28181DeviceGateway gb28181DeviceGateway = new GB28181DeviceGateway();
        return gb28181DeviceGateway;
    }
}
