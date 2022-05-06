package com.cc.network.cp;

import com.alibaba.fastjson.annotation.JSONField;
import com.cc.network.cp.domian.Version;
import com.gow.codec.bytes.DataType;
import com.gow.codec.bytes.serializable.ObjectField;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.cc.network.cp.utils.DataParseUtils.shortToBytes;

@Data
@NoArgsConstructor
public class Header {
    // 消息ID,指令编码
    @ObjectField(dataType = DataType.SHORT)
    private Short messageId;
    // 消息流水号，终端平台独立，发送从0累加
    @ObjectField(dataType = DataType.SHORT)
    private Short sequence;
    // 加密方式，0x00(不加密)，0x01(RSA)
    @ObjectField(dataType = DataType.BYTE)
    private Byte encryption;
    // 登录后请求携带
    @ObjectField(dataType = DataType.SHORT)
    private Short token;
    // 协议版本，BYTE[3]：V1.2.23 对应0x01 0x02 0x23
    @ObjectField
    private Version version;

    public Header(short messageId, short sequence, byte encryption, Short token, Version version) {
        this.messageId = messageId;
        this.sequence = sequence;
        this.token = token;
        this.encryption = encryption;
        this.version = version;
    }

    @JSONField(serialize = false)
    public byte[] getBytes() {
        byte[] ids = shortToBytes(messageId);
        byte[] seq = shortToBytes(sequence);
        if (token == null) {
            return new byte[]{ids[0], ids[1], seq[0], seq[1], encryption, version.getMain(), version.getSub(), version.getFix()};
        } else {
            byte[] ts = shortToBytes(token);
            return new byte[]{ids[0], ids[1], seq[0], seq[1], encryption, ts[0], ts[1], version.getMain(), version.getSub(), version.getFix()};
        }
    }
}
