package com.cc.network.cp.domian.heart;

import com.cc.network.cp.domian.Body;
import com.cc.network.cp.domian.enums.MessageType;
import com.gow.codec.bytes.DataType;
import com.gow.codec.bytes.serializable.ObjectField;
import lombok.Data;

import java.util.List;

/**
 * 心跳消息
 * wcc 2022/5/5
 */
@Data
public class PingMessage implements Body {
    // 桩状态 DWORD
    @ObjectField(dataType = DataType.INT)
    private Integer status;
    // 桩告警信号 DWORD
    @ObjectField(dataType = DataType.INT)
    private Integer warnSignal;
    // 充电枪数量 BYTE
    @ObjectField(dataType = DataType.BYTE)
    private Byte gunNums;
    @ObjectField(loopFieldName = "gunNums")
    private List<GunData> gunData;

    public static PingMessage decode(byte[] body) {
        return new PingMessage();
    }

    @Override
    public MessageType getType() {
        return MessageType.PING;
    }
}
