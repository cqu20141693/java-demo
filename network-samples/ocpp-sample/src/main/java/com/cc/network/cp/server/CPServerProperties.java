package com.cc.network.cp.server;

import io.netty.handler.ssl.SslContext;
import lombok.Data;

/**
 * wcc 2022/4/26
 */
@Data
public class CPServerProperties {
    private String id;
    private String port;
    private String ip;
    private String certId;
    private boolean ssl;
    private SslContext sslContext;
}
