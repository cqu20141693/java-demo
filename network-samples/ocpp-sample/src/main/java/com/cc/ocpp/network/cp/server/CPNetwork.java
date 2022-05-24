package com.cc.ocpp.network.cp.server;

import com.cc.netwok.DefaultNetworkType;
import com.cc.netwok.Network;
import com.cc.netwok.NetworkType;
import lombok.Data;

/**
 * 充电桩网络组件服务端
 * wcc 2022/4/26
 */
@Data
public class CPNetwork implements Network {

    private CPServer cpServer;

    private final String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public NetworkType getType() {
        return DefaultNetworkType.OCPP_QD_SERVER;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public boolean isAutoReload() {
        return false;
    }
}
