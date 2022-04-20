package com.gow.websocket.model;

import lombok.Data;

/**
 * @author gow
 * @date 2021/7/2 0002
 */
@Data
public class MessageBody {
    /**
     * 消息内容
     */
    private String content;
    /**
     * 广播转发的目标地址（告知 STOMP 代理转发到哪个地方）
     */
    private String destination;
}
