package com.cc.bus.topic;

import com.cc.bus.event.SubscribeHandler;
import lombok.Data;

/**
 * 订阅者信息
 * wcc 2022/6/13
 */
@Data
public class SubscribeInfo {
    // 订阅者唯一标识，sessionId/subscribeId
    String subscriberId;
    // share 找到所有的订阅者后选择一个存在的路由
    long features;
    // 在广播订阅信息时，携带ip,后续方便路由
    String brokerIp;
    // 是否是本地利用TopicSubscribe 注解订阅，是否需要路由
    boolean broker;
    // 本地订阅处理器
    SubscribeHandler handler;
    //
}
