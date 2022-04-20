package com.gow.codec.util;

import com.gow.codec.exception.DecodeException;

/**
 * @author gow
 * @date 2021/9/22
 */
public class CodecUtils {

    public static byte getLength() {
        return 0;
    }

    /**
     * 获取数字变长编码:整形数据
     * 低位在前
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
        int bits = lengthBytes.length * 7;
        int operateIndex = 0;
        int multiplier = 1;
        int value = 0;
        int max = 1 << bits;
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
