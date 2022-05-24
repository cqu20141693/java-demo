package com.cc.ocpp.network;

import lombok.Data;
import org.apache.commons.codec.binary.BinaryCodec;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

/**
 * 测试编解码
 * wcc 2022/4/30
 */
@Data
public class TestCodec {

    @Test
    public void testBinaryCodec() {
        String gunId = RandomStringUtils.randomAlphanumeric(32);
        String s = BinaryCodec.toAsciiString(gunId.getBytes());
        System.out.println(s);
    }

    @Test
    void testASCII() {
        String alphanumeric = RandomStringUtils.randomAlphanumeric(32);
        String s = new String(alphanumeric.getBytes(), StandardCharsets.US_ASCII);
        String other = "123##$34";
        String o = new String(other.getBytes(), StandardCharsets.US_ASCII);
        System.out.println(s + ":" + o);
        String han = "中123##$34";
        String h = new String(han.getBytes(), StandardCharsets.US_ASCII);
        System.out.println(han+":"+h);
    }
}
