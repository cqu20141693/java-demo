package com.cc.ocpp.network.cp.domian.login;

import com.cc.ocpp.network.cp.domian.PayLoad;
import com.gow.codec.bytes.DataType;
import com.gow.codec.bytes.serializable.ObjectField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * wcc 2022/4/25
 */
@Data
@NoArgsConstructor
public class ChargeGun implements PayLoad {
    // 枪端口
    @ObjectField(dataType = DataType.BYTE)
    private Byte port;
    // ASCII码，32 充电枪唯一识别码。
    @ObjectField(dataType = DataType.ASCII_LEN, length = 32)
    private String id;

    public ChargeGun(String id, byte port) {
        this.port = port;
        this.id = id;
    }

    @Override
    public byte[] getPayload() {
        byte[] bytes = new byte[33];
        byte[] ids = id.getBytes();
        int index = 0;
        for (int i = 0; i < ids.length; i++) {
            bytes[index++] = ids[i];
        }
        bytes[index] = port;
        return bytes;
    }
}
