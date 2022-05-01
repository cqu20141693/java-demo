package com.cc.network.cp.gateway.device;

import lombok.Data;

import java.nio.channels.Channel;

/**
 * 充电桩会话
 * wcc 2022/4/29
 */
@Data
public class CPSession {
    private Channel channel;
    private String token;
}
