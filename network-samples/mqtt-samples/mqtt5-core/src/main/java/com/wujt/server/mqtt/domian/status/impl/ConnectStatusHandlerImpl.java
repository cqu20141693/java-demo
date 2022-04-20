package com.wujt.server.mqtt.domian.status.impl;

import com.wujt.server.mqtt.domian.status.ConnectStatusHandler;
import com.wujt.server.executor.ProcessExecutorGroup;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wujt
 */
@Component
@Slf4j
public class ConnectStatusHandlerImpl implements ConnectStatusHandler {
    @Autowired
    private ProcessExecutorGroup processExecutorGroup;

    @Override
    public boolean disconnect(Channel channel, String cause) {
        // 异步断开连接
        return false;
    }

    @Override
    public boolean disconnectSync(Channel channel, String cause, Long timestamp) {
        return false;
    }

    @Override
    public void disconnectAllClient() {

    }

    @Override
    public void connectPeriodOnlineCheck(String clientId, String sessionKey) {

    }

    @Override
    public int getClientCount() {
        return 0;
    }
}
