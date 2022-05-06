package com.cc.network.cp.domian.login;

import com.cc.network.cp.domian.Body;
import com.cc.network.cp.domian.Version;
import com.cc.network.cp.domian.enums.MessageType;
import com.gow.codec.bytes.DataType;
import com.gow.codec.bytes.serializable.ObjectField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wcc
 */
@Data
@NoArgsConstructor
public class LoginMessage implements Body {
    // 充电桩id,BYTE[32],ASCII
    @ObjectField(dataType = DataType.STRING, length = 32)
    private String chargePointId;
    // ChargePointType
    @ObjectField(dataType = DataType.BYTE)
    private Byte chargePointType;
    // 桩软件版本 BYTE[3] :V1.2.32 对应 0x01 0x02 0x32
    @ObjectField
    private Version version;
    // 厂商标识 BYTE[5]
    @ObjectField
    private ManufacturerInfoV2 manufacturer;
    @ObjectField(dataType = DataType.BYTE)
    private Byte chargeGunNum;
    @ObjectField(loopFieldName = "chargeGunNum")
    private List<ChargeGun> chargeGuns = new ArrayList<>();
    // NetworkType
    @ObjectField(dataType = DataType.BYTE)
    private Byte network;
    // BYTE[10]  ASCII码
    @ObjectField(dataType = DataType.STRING, length = 10)
    private String community;
    // BYTE[10]  ASCII码
    @ObjectField(dataType = DataType.STRING, length = 10)
    private String baseStation;
    // BYTE[24]  ASCII码
    @ObjectField(dataType = DataType.STRING, length = 24)
    private String ICCID;
    // BYTE[16]  ASCII码
    @ObjectField(dataType = DataType.STRING, length = 16)
    private String IMDI;
    // BYTE[20]  ASCII码
    @ObjectField(dataType = DataType.STRING, length = 20)
    private String bluetooth;

    @Override
    public MessageType getType() {
        return MessageType.LOGIN;
    }

    public static LoginMessage decode(byte[] bytes) {
        return new LoginMessage();
    }
}
