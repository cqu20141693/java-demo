package com.wujt;

import com.wujt.server.netty.MqttBrokerAcceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wujt
 */
@Component
@Slf4j
public class BrokerServer {
    @Autowired
    private MqttBrokerAcceptor acceptor;

    private volatile boolean started = false;

    public boolean isStarted() {
        return started;
    }

    public void startServer() {
        acceptor.initialize();
        started = true;
    }

    public void stopServer() {
        if (started) {
            log.info("Server stopping...");
            acceptor.close();
            started = false;
            log.info("Server stopped");
        }
    }
}
