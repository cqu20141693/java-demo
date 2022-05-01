package com.cc.network.cp.utils;

import org.junit.jupiter.api.Test;

import static com.cc.network.cp.utils.DataParseUtils.bytesToShort;
import static com.cc.network.cp.utils.DataParseUtils.shortToBytes;
import static org.junit.jupiter.api.Assertions.*;

class DataParseUtilsTest {

    @Test
    void testBytesToShort() {
        short s=101;
        byte[] toBytes = shortToBytes(s);
        short toShort = bytesToShort(toBytes[0], toBytes[1]);
        short toShort1 = bytesToShort1(toBytes[0], toBytes[1]);
    }
    public static short bytesToShort1(byte high8, byte low8) {
        //  return (short) (((high8 & 0xff) << 8) + (low8 & 0xff));
        return (short) ((high8 & 0xff << 8) + (low8 & 0xff));
    }
}
