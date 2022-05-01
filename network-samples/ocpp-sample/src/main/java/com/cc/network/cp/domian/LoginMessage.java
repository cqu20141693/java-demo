package com.cc.network.cp.domian;

import com.cc.network.cp.domian.enums.ChargePointType;
import com.cc.network.cp.domian.enums.MessageType;
import com.cc.network.cp.domian.enums.NetworkType;
import lombok.Data;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.cc.network.cp.utils.DataParseUtils.*;

/**
 * @author wcc
 */
@Data
public class LoginMessage implements Body {
    // 充电桩id,BYTE[32],ASCII
    private String chargePointId;
    private ChargePointType chargePointType;
    // 桩软件版本 BYTE[3] :V1.2.32 对应 0x01 0x02 0x32
    private Version version;
    // 厂商标识 BYTE[5]
    private ManufacturerInfo manufacturer;
    //
    private Byte chargeGunNum;
    private List<ChargeGun> chargeGuns = new ArrayList<>();

    private NetworkType network;
    // BYTE[10]  ASCII码
    private String community;
    // BYTE[10]  ASCII码
    private String baseStation;
    // BYTE[24]  ASCII码
    private String ICCID;
    // BYTE[16]  ASCII码
    private String IMDI;
    // BYTE[20]  ASCII码
    private String bluetooth;

    private Integer total;

    public static class Builder {
        // 充电桩id,BYTE[32],ASCII
        private String chargePointId;
        private ChargePointType type;
        // 桩软件版本 BYTE[3] :V1.2.32 对应 0x01 0x02 0x32
        private Version version;
        // 厂商标识 BYTE[5]
        private ManufacturerInfo manufacturer;
        private List<ChargeGun> chargeGuns = new ArrayList<>();

        private NetworkType network;
        // BYTE[10]  ASCII码
        private String community;
        // BYTE[10]  ASCII码
        private String baseStation;
        // BYTE[24]  ASCII码
        private String ICCID;
        // BYTE[16]  ASCII码
        private String IMDI;
        // BYTE[20]  ASCII码
        private String bluetooth;

        public Builder() {
        }

        public Builder chargePointId(String id) {
            assert id.length() == 32 : "charge point id size !=32";
            this.chargePointId = id;
            return this;
        }

        public Builder type(ChargePointType type) {
            this.type = type;
            return this;
        }

        public Builder version(Version version) {
            this.version = version;
            return this;
        }

        public Builder manufacturer(ManufacturerInfo info) {
            this.manufacturer = info;
            return this;
        }

        public Builder network(NetworkType network) {
            this.network = network;
            return this;
        }

        public Builder chargeGuns(List<ChargeGun> chargeGuns) {

            this.chargeGuns = chargeGuns;

            return this;
        }

        public Builder community(String community) {
            assert community.length() == 10 : "community size !=10";
            this.community = community;
            return this;
        }

        public Builder baseStation(String baseStation) {
            assert baseStation.length() == 10 : "base station size !=10";
            this.baseStation = baseStation;
            return this;
        }

        public Builder ICCID(String ICCID) {
            assert ICCID.length() == 24 : "ICCID size !=24";
            this.ICCID = ICCID;
            return this;
        }

        public Builder IMDI(String IMDI) {
            assert IMDI.length() == 16 : "IMDI size !=16";
            this.IMDI = IMDI;
            return this;
        }

        public Builder bluetooth(String bluetooth) {
            assert bluetooth.length() == 20 : "bluetooth size !=20";
            this.bluetooth = bluetooth;
            return this;
        }

        public LoginMessage build() {
            LoginMessage loginMessage = new LoginMessage();
            check();
            int total = 0;
            loginMessage.setChargePointId(this.chargePointId);
            total += 32;
            loginMessage.setChargePointType(this.type);
            total += 1;
            loginMessage.setVersion(this.version);
            total += 3;
            loginMessage.setManufacturer(this.manufacturer);
            total += 5;
            loginMessage.setChargeGunNum((byte) this.chargeGuns.size());
            total += 1;
            loginMessage.setChargeGuns(this.chargeGuns);
            total += chargeGuns.size() * 33;
            loginMessage.setNetwork(this.network);
            total += 1;
            loginMessage.setCommunity(this.community);
            total += 10;
            loginMessage.setBaseStation(this.baseStation);
            total += 10;
            loginMessage.setICCID(this.ICCID);
            total += 24;
            loginMessage.setIMDI(this.IMDI);
            total += 16;
            loginMessage.setBluetooth(this.bluetooth);
            total += 20;
            loginMessage.setTotal(total);
            return loginMessage;
        }

        private void check() {
            assert this.chargePointId != null : "charge point is null";
            assert this.type != null : "type is null";
            assert this.version != null : "version is null";
            assert this.manufacturer != null : "manufacturer is null";
            assert this.chargeGuns != null : "chargeGuns is null";
            assert this.network != null : "network is null";
            assert this.community != null : "community is null";
            assert this.baseStation != null : "baseStation is null";
            assert this.ICCID != null : "ICCID is null";
            assert this.IMDI != null : "IMDI is null";
            assert this.bluetooth != null : "bluetooth is null";

        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public MessageType getType() {
        return MessageType.LOGIN;
    }

    @Override
    public byte[] encode() {
        byte[] body = new byte[total];
        int index = 0;
        byte[] temp = chargePointId.getBytes();
        index = putAndGetIndex(body, index, temp);
        body[index++] = chargePointType.getCode();
        body[index++] = version.getMain();
        body[index++] = version.getSub();
        body[index++] = version.getFix();
        temp = manufacturer.getPayload();
        index = putAndGetIndex(body, index, temp);
        body[index++] = chargeGunNum;
        for (ChargeGun chargeGun : chargeGuns) {
            temp = chargeGun.getPayload();
            index = putAndGetIndex(body, index, temp);
        }
        body[index++] = network.getCode();
        temp = community.getBytes();
        index = putAndGetIndex(body, index, temp);
        temp = baseStation.getBytes();
        index = putAndGetIndex(body, index, temp);
        temp = ICCID.getBytes();
        index = putAndGetIndex(body, index, temp);
        temp = IMDI.getBytes();
        index = putAndGetIndex(body, index, temp);
        temp = bluetooth.getBytes();
        index = putAndGetIndex(body, index, temp);
        if (index == total) {
            return body;
        } else {
            throw new RuntimeException("编码索引错误");
        }

    }

    public static LoginMessage decode(byte[] body) {
        int index = 0;
        byte[] bytes = getBytes(index, index + 32, body);
        index += 32;
        String chargePointId = new String(bytes);
        ChargePointType type = ChargePointType.parseByCode(body[index++]);
        Version version = new Version(body[index++], body[index++], body[index++]);
        bytes = getBytes(index, index + 5, body);
        index += 5;
        ManufacturerInfo manufacturer = new ManufacturerInfo(bytes);
        byte chargeGunNum = body[index++];
        ArrayList<ChargeGun> chargeGuns = new ArrayList<>();
        for (int i = 0; i < parseUnsignedBytes(new byte[]{chargeGunNum}); i++) {
            String id = new String(getBytes(index, index + 32, body));
            index += 32;
            ChargeGun chargeGun = new ChargeGun(id, body[index++]);
            chargeGuns.add(chargeGun);
        }
        NetworkType network = NetworkType.parseByCode(body[index++]);
        String community = new String(getBytes(index, index + 10, body), StandardCharsets.US_ASCII);
        index += 10;
        String baseStation = new String(getBytes(index, index + 10, body), StandardCharsets.US_ASCII);
        index += 10;
        String ICCID = new String(getBytes(index, index + 24, body), StandardCharsets.US_ASCII);
        index += 24;
        String IMDI = new String(getBytes(index, index + 16, body), StandardCharsets.US_ASCII);
        index += 16;
        String bluetooth = new String(getBytes(index, index + 20, body), StandardCharsets.US_ASCII);
        index += 20;
        if (index != body.length) {
            throw new RuntimeException("login message decode failed");
        }
        return LoginMessage.builder()
                .chargePointId(chargePointId)
                .type(type)
                .version(version)
                .manufacturer(manufacturer)
                .chargeGuns(chargeGuns)
                .network(network)
                .community(community)
                .baseStation(baseStation)
                .IMDI(IMDI)
                .ICCID(ICCID)
                .bluetooth(bluetooth)
                .build();
    }


}
