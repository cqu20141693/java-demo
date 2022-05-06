package com.cc.network.cp.domian.heart;

import com.gow.codec.bytes.DataType;
import com.gow.codec.bytes.serializable.ObjectField;
import lombok.Data;

/**
 * 单项交流数据域
 * wcc 2022/5/5
 */
@Data
public class SingleACField {
    //充电电压U WORD
    @ObjectField(dataType = DataType.SHORT)
    private Short u;
    //充电电流I DWORD
    @ObjectField(dataType = DataType.INT)
    private Integer i;
    // 读数电表 读数电表 DWORD
    @ObjectField(dataType = DataType.INT)
    private Integer meterReading;

    public static SingleACField parse(byte[] bytes) {
        return new SingleACField();
    }
}
