package com.wujt.server.mqtt.domian.store.relation;

import com.alibaba.fastjson.JSONObject;
import com.wujt.config.MqttProtocolConfig;
import com.wujt.server.mqtt.domian.ConnectionDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author wujt
 */
@Component
@Slf4j
public class ConnectRelation {
    @Autowired
    private MqttProtocolConfig mqttConfig;

    /**
     * ClientId --> channel,
     */
    private ConcurrentHashMap<String, ConnectionDescriptor> clientIdMap;

    @PostConstruct
    public void init() {
        clientIdMap = new ConcurrentHashMap<>(mqttConfig.getMaxConnection() + 10000, 0.95f, 32);
    }

    public ConnectionDescriptor getConnByClientId(String clientId) {
        if (StringUtils.isEmpty(clientId)) {
            return null;
        }
        return clientIdMap.get(clientId);
    }

    /**
     * 检查是否存在链接
     *
     * @return
     */
    public boolean containConnRelation(String clientId) {
        if (StringUtils.isEmpty(clientId)) {
            return false;
        }
        return clientIdMap.containsKey(clientId);
    }

    /**
     * @param clientId
     * @param connectionDescriptor
     * @return pre ConnectionDescriptor
     */
    public ConnectionDescriptor recordRelation(String clientId, ConnectionDescriptor connectionDescriptor) {
        return clientIdMap.putIfAbsent(clientId, connectionDescriptor);
    }

    /**
     * related conn
     *
     * @param clientId
     * @return
     */
    public ConnectionDescriptor removeRelation(String clientId) {
        return clientIdMap.remove(clientId);
    }

    /**
     * 对所有链接执行指定动作
     *
     * @param action
     */
    public void executeForEachRelation(Consumer<ConnectionDescriptor> action) {
        //依次执行 避免并发。
        clientIdMap.forEach((k, v) -> action.accept(v));
    }

    public void printRelation() {

        log.info("current relations={}", JSONObject.toJSONString(clientIdMap));
    }
}
