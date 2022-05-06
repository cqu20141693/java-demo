package com.gow.codec.bytes.model;

import com.gow.codec.bytes.DataType;
import com.gow.codec.bytes.serializable.ObjectField;
import lombok.Data;

/**
 * wcc 2022/5/5
 */
@Data
public class PongMessage {

    //WORD 对应的终端消息的流水号
    @ObjectField(dataType = DataType.SHORT)
    private Short sequence;
    //BYTE 0：成功/确认 1：失败 -- 平台处理该消息失败
    // 2：消息有误 -- 消息校验错误、消息长度有误（如果校验错误3次，桩重新登录）
    @ObjectField(dataType = DataType.BYTE)
    private Byte success;
    // DWORD 到当前的秒数
    @ObjectField(dataType = DataType.INT)
    private Integer time;
    @ObjectField(dataType = DataType.ENUM)
    private ChargePointType type;
    @ObjectField(dataType = DataType.STRING)
    private String chargePointId;
}
