package com.cc.bus.event;

/**
 * 订阅处理器
 * wcc 2022/6/13
 */
public interface SubscribeHandler {
    /**
     * 事件处理
     *
     * @param subscribeId 订阅者唯一标识，用于本地session 查找
     * @param event
     */
    void subscribe(String subscribeId, Object event);
}
