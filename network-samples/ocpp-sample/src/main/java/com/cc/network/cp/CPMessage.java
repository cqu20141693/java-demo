package com.cc.network.cp;

import com.alibaba.fastjson.annotation.JSONField;
import com.cc.network.cp.domian.Body;
import com.cc.network.cp.domian.TokenGenerator;
import com.cc.network.cp.domian.Version;
import com.cc.network.cp.domian.enums.MessageType;
import com.gow.codec.bytes.DataType;
import com.gow.codec.bytes.serializable.ObjectField;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;

import static com.cc.network.cp.utils.DataParseUtils.*;

@Data
public class CPMessage {
    // 转义标识
    @ObjectField(dataType = DataType.BYTE)
    private Byte flag;
    @ObjectField
    private Header header;
    @ObjectField(dataType = DataType.SHORT)
    private Short length;
    @ObjectField(classMethod = "getBodyClass")
    private Body body;
    @ObjectField(dataType = DataType.BYTE)
    private Byte checkSum;
    // 等于flag
    @ObjectField(dataType = DataType.BYTE)
    private Byte endFlag;
    @JSONField(serialize = false)
    private int total;

    public Class<?> getBodyClass() {
        MessageType type = MessageType.parseByMessageId(header.getMessageId());
        return type.getzClass();
    }

    public CPMessage() {
    }


    public CPMessage(byte flag, MessageType type, int seq, byte encryption, Short token, Version version, Body body) {
        assert type != null : "type can not be null";
        this.flag = flag;
        total++;
        assert (type == MessageType.LOGIN && token == null) || TokenGenerator.validate(token) : "token error";
        this.header = new Header(type.getMessageId(), (short) (seq & 0xffff), encryption, token, version);
        total += header.getBytes().length;
        byte[] encodeBody = body.getBody();
        int bodyLen = encodeBody.length;
        if (bodyLen > 0xffff) {
            throw new RuntimeException("消息体过长，不支持报文");
        }
        this.length = (short) (bodyLen & 0xffff);
        total += 2;
        total += bodyLen;
        this.body = body;
        byte[] t = token == null ? null : shortToBytes(token);
        this.checkSum = getSum(encodeBody, t);
        this.endFlag = flag;
        total += 2;
    }

    public ByteBuf toByteBuf() {
        byte[] payload = new byte[total];
        int index = 0;
        payload[index++] = flag;
        byte[] headers = header.getBytes();
        for (int i = 0; i < headers.length; i++) {
            payload[index++] = headers[i];
        }
        byte[] len = shortToBytes(this.length);
        payload[index++] = len[0];
        payload[index++] = len[1];

        byte[] encodeBody = this.body.getBody();
        int counter = 0;
        if (flag == 0x7e) {
            ArrayList<Byte> bytes = new ArrayList<>();
            for (int i = 0; i < encodeBody.length; i++) {
                if (encodeBody[i] == 0x7e) {
                    counter++;
                    bytes.add((byte) 0x7d);
                    bytes.add((byte) 0x02);
                } else if (encodeBody[i] == 0x7d) {
                    counter++;
                    bytes.add((byte) 0x7d);
                    bytes.add((byte) 0x01);
                } else {
                    bytes.add(encodeBody[i]);
                }
            }
            for (int i = 0; i < bytes.size(); i++) {
                payload[index++] = bytes.get(i);
            }
        } else {
            for (int i = 0; i < encodeBody.length; i++) {
                payload[index++] = encodeBody[i];
            }
        }

        payload[index++] = checkSum;
        payload[index++] = endFlag;
        if (index != total + counter) {
            throw new RuntimeException("编码异常");
        }
        return Unpooled.wrappedBuffer(payload);
    }

    public static CPMessage decode(byte[] payload) {
        int index = 0;
        byte flag = payload[index++];
        assert flag == payload[payload.length - 1] : "标识位解析错误";
        MessageType type = MessageType.parseByMessageId(bytesToShort(payload[index++], payload[index++]));
        assert type != null : "type not support";
        short seq = bytesToShort(payload[index++], payload[index++]);
        byte encryption = payload[index++];
        Short token = null;
        if (type != MessageType.LOGIN) {
            token = bytesToShort(payload[index++], payload[index++]);
            assert TokenGenerator.validate(token) : "token error";
        } else {
            index += 2;
        }
        Version version = new Version(payload[index++], payload[index++], payload[index++]);
        short length = bytesToShort(payload[index++], payload[index++]);
        LinkedList<Byte> bytes = new LinkedList<>();
        if (flag == 0x7e) {
            // 0x7e<—— >0x7d 后紧跟一个 后紧跟一个 后紧跟一个 0x02
            //0x7d<—— >0x7d 后紧跟一个 后紧跟一个 后紧跟一个 0x01
            // 编码生成校验码，转义；解码先转义，再校验
            byte pre = 0;
            for (int i = 0; i < length; i++) {
                if (pre == 0x7d) {
                    if (payload[index++] == 0x02) {
                        pre = 0x02;
                        bytes.removeLast();
                        bytes.add((byte) 0x7e);
                        continue;
                    } else if (payload[index++] == 0x01) {
                        pre = 0x01;
                        continue;
                    } else {
                        throw new RuntimeException("报文转义错误，index=" + index);
                    }
                }
                bytes.add(payload[index++]);
                pre = payload[index - 1];
            }
        } else {
            for (int i = 0; i < length; i++) {
                bytes.add(payload[index++]);
            }
        }
        byte[] body = new byte[bytes.size()];
        for (int i = 0; i < body.length; i++) {
            body[i] = bytes.get(i);
        }
        if (type == MessageType.LOGIN || checkSum(body, shortToBytes(token), payload[index])) {
            return new CPMessage(flag, type, seq, encryption, token, version, type.getDecoder().apply(body));
        } else {
            throw new RuntimeException("检验码错误");
        }

    }

}
