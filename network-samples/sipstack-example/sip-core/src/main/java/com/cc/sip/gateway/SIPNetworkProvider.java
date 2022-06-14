package com.cc.sip.gateway;

import com.cc.netwok.*;
import com.cc.sip.SipConfig;
import lombok.Data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * sip 网络提供器
 * wcc 2022/5/25
 */
@Data
public class SIPNetworkProvider implements NetworkProvider<SipConfig> {
    @Nonnull
    @Override
    public NetworkType getType() {
        return null;
    }

    @Nonnull
    @Override
    public Network createNetwork(@Nonnull SipConfig properties) {
        return null;
    }

    @Override
    public void reload(@Nonnull Network network, @Nonnull SipConfig properties) {

    }

    @Nullable
    @Override
    public ConfigMetadata getConfigMetadata() {
        return null;
    }

    @Nonnull
    @Override
    public SipConfig createConfig(@Nonnull NetworkProperties properties) {
        return null;
    }
}
