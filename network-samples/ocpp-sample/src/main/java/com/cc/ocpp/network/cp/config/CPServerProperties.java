package com.cc.ocpp.network.cp.config;

import io.netty.handler.ssl.SslContext;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * wcc 2022/4/26
 */
@Data
@Configuration
@ConfigurationProperties("wcc.ocpp")
public class CPServerProperties {
    private String id;
    private Integer port = 9000;
    private Integer publicPort = 9000;
    private String ip = "0.0.0.0";
    private String publicIp = "127.0.0.1";
    private Short keepalive = 120;
    private Short reportInterval = 120;
    private String certId;
    private boolean ssl;
    private SslContext sslContext;
}
