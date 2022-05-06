package com.cc.network.cp.domian.heart;

import com.cc.network.cp.domian.Body;
import com.cc.network.cp.domian.enums.MessageType;
import lombok.Data;

/**
 * 桩心跳响应
 * wcc 2022/5/5
 */
@Data
public class PongMessage implements Body {
    //WORD 对应的终端消息的流水号
    private short sequence;
    //BYTE 0：成功/确认 1：失败 -- 平台处理该消息失败
    // 2：消息有误 -- 消息校验错误、消息长度有误（如果校验错误3次，桩重新登录）
    private byte success;
    // DWORD 到当前的秒数
    private Integer time;

    public static PongMessage decode(byte[] bytes) {
      return   new PongMessage();
    }

    @Override
    public MessageType getType() {
        return MessageType.PONG;
    }
}
