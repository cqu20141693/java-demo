package com.cc.ocpp.network.cp.domian.heart;

import com.cc.ocpp.network.cp.domian.enums.GunType;
import com.gow.codec.bytes.DataType;
import com.gow.codec.bytes.serializable.ObjectField;
import lombok.Data;

/**
 * wcc 2022/5/5
 */
@Data
public class GunData {
    // 端口号
    @ObjectField(dataType = DataType.BYTE)
    private Byte port;
    // 充电枪类型 BYTE GunType
    @ObjectField(dataType = DataType.BYTE)
    private Byte gunType;
    //开关信号 DWORD
    @ObjectField(dataType = DataType.INT)
    private Integer switchSignal;
    //开关信号 DWORD
    @ObjectField(dataType = DataType.INT)
    private Integer warnSignal;
    // 工作状态 BYTE
    @ObjectField(dataType = DataType.BYTE)
    private Byte workStatus;
    // 数据域，根据gunType 定义
    @ObjectField(dataType = DataType.OBJECT, classMethod = "getDataClass")
    private Object data;

    public Class<?> getDataClass() {
        if (gunType == GunType.single_phase_ac.getCode()) {
            return SingleACField.class;
        } else if (gunType == GunType.three_phase_ac.getCode() ||
                gunType == GunType.direct.getCode()) {
            return DirectField.class;
        } else if (gunType == GunType.wireless.getCode()) {
            return WirelessField.class;
        } else {
            throw new RuntimeException(String.format("gunType=%d error", gunType));
        }
    }

}
