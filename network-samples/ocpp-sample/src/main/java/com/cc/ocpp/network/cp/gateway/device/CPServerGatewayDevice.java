package com.cc.ocpp.network.cp.gateway.device;

import com.cc.netwok.NetworkType;
import com.cc.netwok.gateway.DeviceGateway;
import com.cc.netwok.gateway.Message;
import com.cc.netwok.gateway.Transport;
import lombok.Data;

/**
 * 充电桩网关组件
 * wcc 2022/4/26
 */
@Data
public class CPServerGatewayDevice implements DeviceGateway {
    @Override
    public String getId() {
        return null;
    }

    @Override
    public Transport getTransport() {
        return null;
    }

    @Override
    public NetworkType getNetworkType() {
        return null;
    }

    @Override
    public Message onMessage() {
        return null;
    }

    @Override
    public Void startup() {
        return null;
    }

    @Override
    public Void pause() {
        return null;
    }

    @Override
    public Void shutdown() {
        return null;
    }
}
