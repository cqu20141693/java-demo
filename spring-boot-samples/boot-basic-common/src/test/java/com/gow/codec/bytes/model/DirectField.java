package com.gow.codec.bytes.model;

import com.gow.codec.bytes.DataType;
import com.gow.codec.bytes.serializable.ObjectField;
import lombok.Data;

/**
 * 三项交流和直流数据域
 * wcc 2022/5/5
 */
@Data
public class DirectField {

    //充电电压Ua WORD
    @ObjectField(dataType = DataType.SHORT)
    private Short ua;
    //充电电压Ub WORD
    @ObjectField(dataType = DataType.SHORT)
    private Short ub;
    // 充电电压Uc WORD
    @ObjectField(dataType = DataType.SHORT)
    private Short uc;
    //充电电流Ia DWORD
    @ObjectField(dataType = DataType.SHORT)
    private Short ia;
    //充电电流Ib DWORD
    @ObjectField(dataType = DataType.SHORT)
    private Short ib;
    //充电电流Ic DWORD
    @ObjectField(dataType = DataType.SHORT)
    private Short ic;

    public static DirectField parse(byte[] bytes){
        return new DirectField();
    }
}
