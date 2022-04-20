package com.wujt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author wujt
 */
@Configuration
@ConfigurationProperties(prefix = "mqtt")
@RefreshScope
@Data
public class MqttProtocolConfig {

    /**
     * the timeout second between tcp third handshake and the mqtt Connect message
     */
    private int connectTimeout = 10;
    /**
     * the listening host of mqtt server,default value "0.0.0.0"
     */
    private String host = "0.0.0.0";
    /**
     * the listen port of the mqtt server,default value:1883
     */
    private int port = 2883;
    /**
     * Does it support websocket,default value: false;
     */
    private boolean enableWebsocket = false;
    /**
     * the listen ws  port of the mqtt server,default value:8884
     */
    private int wsPort = 2884;
    /**
     * if the broker is enable enableSubscribe message. if it is false, then if this broker receive subscripe message,
     * it will disconnect the client.
     */
    private boolean enableSubscribe = false;
    // does qos 2 support
    private boolean enableQos2 = false;
    /**
     * if this broker allow anonymous login,default value: false
     */
    private boolean anonymous = false;
    /**
     * the mininal seconds of the hearbeat, if the client heart beat is less than this value, the server will refuse the connection
     * default value: 120
     */
    private int minHeartBeatSecond = 120;

    /**
     * the thread size used to listen,default value: 2
     */
    private int bossGroupSize = 2;
    /**
     * the thread size used as worker,default value: 4
     */
    private int workerGroupSize = 4;
    /**
     * the thread size use as backend thread pool,default value: 4
     */
    private int processGroupSize = 4;
    // broker支持最大的连接数
    private int maxConnection;
}
