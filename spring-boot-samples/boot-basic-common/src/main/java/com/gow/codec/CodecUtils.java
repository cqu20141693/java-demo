package com.gow.codec;

import com.gow.codec.exception.DecodeException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CodecUtils {
    private final static Logger logger = LoggerFactory.getLogger(CodecUtils.class);

    public static String sha256(String str) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeBase64String(hash);
        } catch (NoSuchAlgorithmException e) {
            logger.error("", e);
            return null;
        }
    }

    /**
     * 获取数字变长编码:整形数据
     * 低位在前,小端
     *
     * @param number int
     * @return
     */
    public static byte[] getVariableNumberBytes(int number) {
        int size = 0;
        int temp = number;
        do {
            temp /= 128;
            size++;
        } while (temp > 0);

        byte[] bytes = new byte[size];
        int index = 0;
        do {
            byte digit = (byte) (number & 0xff);
            number /= 128;
            if (number > 0) {
                digit |= 0x80;
            }
            bytes[index++] = digit;

        } while (index < size);
        return bytes;
    }

    /**
     * 低位在前
     *
     * @param lengthBytes
     * @return
     */
    public static int decode(byte[] lengthBytes) {
        int offset=0;
        int operateIndex =offset;
        int multiplier = 1;
        int value = 0;
        byte encodeByte;
        do {
            encodeByte = lengthBytes[operateIndex];
            value += (encodeByte & 0x7f) * multiplier;
            multiplier = multiplier << 7;
            operateIndex++;
        } while ((encodeByte & 0x80) != 0);

        if (operateIndex != lengthBytes.length) {
            throw new DecodeException("variable number parse error.");
        }

        return value;
    }

}
