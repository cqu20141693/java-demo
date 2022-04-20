package com.wujt.server.mqtt.domian.status;

import io.netty.channel.Channel;

/**
 * @author wujt
 */
public interface ConnectStatusHandler {
    /**
     * 强制断开Channel 连接,异步
     *
     * @param channel
     * @param cause
     * @return 成功返回true，失败返回false
     */
    boolean disconnect(Channel channel, String cause);

    /**
     * 强制断开Channel 连接 同步执行
     *
     * @param channel
     * @param cause
     * @param timestamp
     * @return
     */
    boolean disconnectSync(Channel channel, String cause, Long timestamp);


    /**
     * 断开所有连接
     */
    void disconnectAllClient();

    /**
     * 设备周期在线检查
     *
     * @param clientId
     * @param sessionKey
     */
    void connectPeriodOnlineCheck(String clientId, String sessionKey);

    /**
     * 返回当前broker上的客户端链路的数量
     *
     * @return 当前在线的device的数量
     */
    int getClientCount();


}
