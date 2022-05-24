package com.cc.ocpp.network.cp.domian.login;

import com.cc.ocpp.network.cp.domian.enums.ManufacturerType;
import com.gow.codec.bytes.DataType;
import com.gow.codec.bytes.serializable.ObjectField;
import lombok.Data;

/**
 * 厂商标识信息
 * wcc 2022/4/25
 */
@Data
public class ManufacturerInfoV2 {

    /**
     * {@link ManufacturerType}
     */
    @ObjectField(dataType = DataType.ASCII_LEN, length = 2)
    private String type;
    // （2byte ASCII码）：“01”（交流私桩1型），“02”（交流运营1型），“03”（直流桩1型），“04”（三相交流1型
    //ProductType
    @ObjectField(dataType = DataType.ASCII_LEN, length = 2)
    private String productType;
    // 1 byte, 功率 PowerType
    @ObjectField(dataType = DataType.BYTE)
    private Byte power;

}
