package com.cc.network.cp.domian;

import com.alibaba.fastjson.annotation.JSONField;
import com.gow.codec.bytes.DataType;
import com.gow.codec.bytes.serializable.ObjectField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 充电桩版本
 * wcc 2022/4/25
 */
@Data
@NoArgsConstructor
public class Version implements PayLoad {
    public static final Version default_version = new Version((byte) 1, (byte) 2, (byte) 23);
    @ObjectField(dataType = DataType.BYTE)
    private Byte main;
    @ObjectField(dataType = DataType.BYTE)
    private Byte sub;
    @ObjectField(dataType = DataType.BYTE)
    private Byte fix;

    public Version(byte main, byte sub, byte fix) {
        this.main = main;
        this.sub = sub;
        this.fix = fix;
    }

    @Override
    public byte[] getPayload() {
        return new byte[]{main, sub, fix};
    }

    @JSONField(serialize = false)
    public String getValue() {
        return String.format("V%d.%d.%d", main, sub, fix);
    }

}
