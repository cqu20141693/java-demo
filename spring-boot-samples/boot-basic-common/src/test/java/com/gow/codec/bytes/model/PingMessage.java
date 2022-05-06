package com.gow.codec.bytes.model;

import com.gow.codec.bytes.DataType;
import com.gow.codec.bytes.serializable.ObjectField;
import lombok.Data;

import java.util.List;

/**
 * 心跳消息
 * wcc 2022/5/5
 */
@Data
public class PingMessage {
    // 桩状态
    @ObjectField(dataType = DataType.INT)
    private Integer status;
    // 桩告警信号
    @ObjectField(dataType = DataType.SHORT)
    private Short warnSignal;
    // 充电枪数量
    @ObjectField(dataType = DataType.BYTE)
    private Byte gunNums;
    // 充电枪数量
    @ObjectField(dataType = DataType.STRING, length = 8)
    private String description;

    @ObjectField(dataType = DataType.OBJECT, loopFieldName = "gunNums")
    private List<GunData> gunData;

}
