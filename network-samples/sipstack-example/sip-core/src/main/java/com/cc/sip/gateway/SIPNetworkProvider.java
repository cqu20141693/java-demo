package com.cc.sip.gateway;

import com.alibaba.fastjson.JSONObject;
import com.cc.netwok.*;
import com.cc.sip.SipConfig;
import com.cc.sip.SipProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * sip 网络提供器
 * wcc 2022/5/25
 */
@Data
@Slf4j
public class SIPNetworkProvider implements NetworkProvider<SipConfig> {
    @Nonnull
    @Override
    public NetworkType getType() {
        return null;
    }

    @Nonnull
    @Override
    public Network createNetwork(@Nonnull SipConfig config) {
        log.info("create sip Network :{}", config);
        SipProperties properties = JSONObject.parseObject(JSONObject.toJSONString(config), SipProperties.class);
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
