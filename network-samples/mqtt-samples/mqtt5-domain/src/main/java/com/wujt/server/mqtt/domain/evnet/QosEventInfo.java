package com.wujt.server.mqtt.domain.evnet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * qos0 默认发送一次，所以只要调用发送接口成功表示事件
 * <p>
 * qos 1 事件:
 * qos 1 事件发送前需要先将数据保存到平台，
 * qos 1 接入层发送pub事件，当发送成功后发送一个pub send 事件通知
 * qos 1 client发送，接入层接收ack 后， 发送一个ack 事件通知
 * qos 1 接入层expired 后，发送一个expired 事件通知，是否要重新发送交给上层处理
 * <p>
 * qos 2 事件：
 * qos2 事件发送前，需要先将数据保存到平台
 * qos2 接入层发送pub事件，发送成功后发送一个pub send事件
 * qos2 接入层pub expired后，发送一个pub expired事件，是否重新发送交给上层处理
 * qos2 client发送pubrec事件，当接入机接收后，发送一个pubrec 事件通知,并紧接着发送pubrel
 * qos2 接入层pubrel对pubrec响应后，发送一个pubrel send事件通知
 * qos2 接入机pubrel expired 后，发送一个pubrel expired 事件通知，是否重新发送pubrel 交给上传处理
 * qos2 client发送pubcomp对pubrel响应，接入机接收后，发送一个pubcomp 的事件
 *
 * @author wujt
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QosEventInfo {

    // qos 类型
    private Integer qos;
    /**
     * 业务类型： eg command
     */
    private String type;
    /**
     * 业务对应的唯一标识： eg commandId
     */
    private String businessId;
    // qos topic
    private String topic;
    // 消息负载
    private byte[] payload;
    // 发送链路唯一标识
    private String clientId;
    /**
     * qos1 消息处理状态：send,ack,expired
     * qos2
     */
    private Integer status;
    /**
     * meg id
     */
    private volatile int messageId = 0;
    // 时间
    private long time;


}
