package com.cc.network.cp.domian;

import com.cc.network.cp.domian.enums.ManufacturerType;
import com.cc.network.cp.domian.enums.PowerType;
import com.cc.network.cp.domian.enums.ProductType;
import lombok.Data;

import java.nio.charset.StandardCharsets;

/**
 * 厂商标识信息
 * wcc 2022/4/25
 */
@Data
public class ManufacturerInfo implements PayLoad {

    /**
     * {@link com.cc.network.cp.domian.enums.ManufacturerType}
     */
    private ManufacturerType type;
    // （2byte ASCII码）：“01”（交流私桩1型），“02”（交流运营1型），“03”（直流桩1型），“04”（三相交流1型
    private ProductType productType;
    // 1 byte, 功率
    private PowerType power;

    public ManufacturerInfo(byte[] value) {
        assert value != null && value.length == 5 : "厂商信息错误";
        ManufacturerType type = ManufacturerType.parseByCode(new String(new byte[]{value[0], value[1]}, StandardCharsets.US_ASCII));
        assert type != null : "厂商信息错误";
        ProductType productType = ProductType.parseByCode(new String(new byte[]{value[2], value[3]}, StandardCharsets.US_ASCII));
        assert productType != null : "厂商信息错误";
        PowerType powerType = PowerType.parseByCode(value[4]);
        assert powerType != null : "厂商信息错误";
        this.type = type;
        this.productType = productType;
        this.power = powerType;
    }

    public ManufacturerInfo(ManufacturerType type, ProductType productType, PowerType power) {
        this.type = type;
        this.productType = productType;
        this.power = power;
    }

    @Override
    public byte[] getPayload() {
        byte[] bytes = new byte[5];
        byte[] codeBytes = this.type.getCode().getBytes();
        byte[] product = this.productType.getCode().getBytes();
        bytes[0] = codeBytes[0];
        bytes[1] = codeBytes[1];
        bytes[2] = product[0];
        bytes[3] = product[1];
        bytes[4] = power.getCode();
        return bytes;
    }

}
