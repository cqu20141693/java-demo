package com.cc.network;

import com.cc.network.cp.domian.NetworkPropertiesEntity;
import com.cc.network.cp.server.CPNetworkProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.cc.network.DefaultNetworkType.OCPP_QD_SERVER;

class GatewayDeviceTest {

    private final Map<String, NetworkProvider<Object>> providerSupport = new ConcurrentHashMap<>();
    private final Map<String, Network> networkMap = new ConcurrentHashMap<>();

    @BeforeAll
    public void init() {
        CPNetworkProvider cpNetworkProvider = new CPNetworkProvider();
        NetworkProvider provider = cpNetworkProvider;
        providerSupport.put(cpNetworkProvider.getType().getId(), provider);


    }

    @Test
    @DisplayName("测试网关组件启动")
    public void testCreateGateWayDevice() {
        // 模拟实体创建
        DefaultNetworkType type = OCPP_QD_SERVER;
        NetworkProvider<Object> provider = providerSupport.get(type);
        assert provider != null : "provider is null";
        Mono.just(createEntity(type, true, new HashMap<>()))
                .map(this::toProperties)
                .filter(NetworkProperties::isEnabled)
                .flatMap(provider::createConfig)
                .map(conf -> this.doCreate(provider, conf));
    }

    private Object doCreate(NetworkProvider<Object> provider, Object conf) {
        return null;
    }

    private NetworkProperties toProperties(NetworkPropertiesEntity entity) {
        NetworkProperties properties = new NetworkProperties();
        BeanUtils.copyProperties(entity, properties);
        return properties;
    }


    private NetworkPropertiesEntity createEntity(NetworkType networkType, boolean enable, Map<String, Object> conf) {
        return NetworkPropertiesEntity.builder()
                .id(networkType.getId())
                .name(networkType.getName())
                .enabled(enable)
                .configurations(conf)
                .build();
    }


}
