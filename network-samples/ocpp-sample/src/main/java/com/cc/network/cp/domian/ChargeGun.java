package com.cc.network.cp.domian;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * wcc 2022/4/25
 */
@Data
public class ChargeGun implements PayLoad{
    // ASCII码，32 充电枪唯一识别码。
    private String id;
    // 枪端口
    private byte port;

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
