package com.cc.network.cp.domian;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 充电桩版本
 * wcc 2022/4/25
 */
@Data
public class Version implements PayLoad {
    public static final Version default_version = new Version((byte) 1, (byte) 2, (byte) 23);
    private byte main;
    private byte sub;
    private byte fix;

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
