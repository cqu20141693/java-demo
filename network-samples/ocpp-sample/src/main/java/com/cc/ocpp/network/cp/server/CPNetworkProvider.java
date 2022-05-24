package com.cc.ocpp.network.cp.server;

import com.cc.netwok.*;
import com.cc.ocpp.network.cp.config.CPServerProperties;
import io.netty.handler.ssl.SslContext;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.cc.netwok.DefaultNetworkType.OCPP_QD_SERVER;

/**
 * 充电桩网络提供组件
 * wcc 2022/4/26
 */
@Data
public class CPNetworkProvider implements NetworkProvider<CPServerProperties> {
    @Nonnull
    @Override
    public NetworkType getType() {
        return OCPP_QD_SERVER;
    }

    @Nonnull
    @Override
    public Network createNetwork(@Nonnull CPServerProperties properties) {
        return null;
    }

    @Override
    public void reload(@Nonnull Network network, @Nonnull CPServerProperties properties) {

    }

    @Nullable
    @Override
    public ConfigMetadata getConfigMetadata() {
        return null;
    }

    @Nonnull
    @Override
    public CPServerProperties createConfig(@Nonnull NetworkProperties properties) {
        CPServerProperties config = new CPServerProperties();
        BeanUtils.copyProperties(properties.getConfigurations(), config);
        config.setId(properties.getId());
        if (config.isSsl()) {
            config.setSslContext(createSsl(config.getCertId()));
        }
        return config;
    }

    private SslContext createSsl(String certId) {
        return null;
    }
}
