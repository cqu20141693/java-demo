package com.cc.ocpp.network.cp.domian;

import com.alibaba.fastjson.annotation.JSONField;
import com.cc.ocpp.network.cp.domian.enums.MessageType;

/**
 * 消息体
 * wcc 2022/4/29
 */
public interface Body extends Encoder {

    @JSONField(serialize = false)
    default byte[] getBody() {
        return encode();
    }

    @JSONField(serialize = false)
    MessageType getType();

    @Override
    default byte[] encode() {
        return null;
    }

}
