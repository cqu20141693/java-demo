package com.wujt.server.model;


import com.wujt.server.mqtt.domain.client.MqttDeviceClientInfo;
import lombok.Data;

/**
 * DeviceEvent类的描述 : channel 连接，断开，心跳三种事件
 *
 * @author wujt instead of SysLog
 */
@Data
public class ChannelEventInfo extends MqttDeviceClientInfo {


    /**
     * 时间戳(timestamp)
     */
    private long time;
    /**
     * 连接事件, 参见{@link ConnEventEnum}
     */
    private int event;

    // 路由表信息

    /**
     * Service instance ip, need to be saved in routing table
     */
    private String ip;
    /**
     * port : 需要保存到路由表， 服务提供方的端口
     */
    private int port;

}
