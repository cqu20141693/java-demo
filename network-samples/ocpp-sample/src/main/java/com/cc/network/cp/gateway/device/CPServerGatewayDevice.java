package com.cc.network.cp.gateway.device;

import com.cc.network.NetworkType;
import com.cc.network.cp.gateway.DeviceGateway;
import com.cc.network.cp.gateway.Message;
import com.cc.network.cp.gateway.Transport;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Flux<Message> onMessage() {
        return null;
    }

    @Override
    public Mono<Void> startup() {
        return null;
    }

    @Override
    public Mono<Void> pause() {
        return null;
    }

    @Override
    public Mono<Void> shutdown() {
        return null;
    }
}
