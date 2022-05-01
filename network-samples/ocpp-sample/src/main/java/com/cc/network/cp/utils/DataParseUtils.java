package com.cc.network.cp.utils;

public class DataParseUtils {
    public static int parseUnsignedByte(byte b) {
        return Byte.toUnsignedInt(b);
    }


    public static long parseUnsignedBytes(byte[] bytes) {
        long sum = 0;
        int bits = 0;
        for (int i = bytes.length - 1; i >= 0; i--, bits += 8) {
            sum = sum + (bytes[i] & 0xff) * ((long) 1 << bits);
        }
        return sum;
    }

    public static short bytesToShort(byte high8, byte low8) {
        //  return (short) (((high8 & 0xff) << 8) + (low8 & 0xff));
        return (short) (((high8 & 0xff) << 8) + (low8 & 0xff));
    }


    public static short[] bytesToShorts(byte[] body, int index, int total) {
        assert (total - index) % 2 == 0 : "bytes to shorts param error";
        short[] shorts = new short[(total - index) / 2];
        for (int i = index, j = 0; i < total; i += 2, j++) {
            shorts[j] = bytesToShort(body[i], body[i + 1]);
        }
        return shorts;
    }

    public static byte[] getBytes(int start, int end, byte[] body) {
        byte[] bytes = new byte[end - start];
        for (int i = 0; start < end; i++) {
            bytes[i] = body[start++];
        }
        return bytes;
    }

    public static double toDword(int value, byte pow) {
        int divisor = 1;
        for (int i = 0; i < pow; i++) {
            divisor *= 10;
        }
        return value / divisor;
    }

    public static boolean checkSum(byte[] body, byte[] token, byte sum) {
        byte result = getSum(body, token);
        return result == sum;
    }

    public static byte[] shortToBytes(short s) {
        return new byte[]{(byte) ((s >> 8) & 0xff), (byte) (s & 0xff)};
    }

    public static byte[] intToBytes(int s) {
        return new byte[]{(byte) ((s >> 24) & 0xff), (byte) ((s >> 16) & 0xff), (byte) ((s >> 8) & 0xff), (byte) (s & 0xff)};
    }

    public static int putAndGetIndex(byte[] body, int index, byte[] temp) {
        for (int i = 0; i < temp.length; i++) {
            body[index++] = temp[i];
        }
        return index;
    }

    /**
     * 从消息长度后字节（不包括消息长度）开始，同后一字节 异或直到校验码前个字节再按开始，再按异或 token值（登录不需要），最终获得的，占用一个字节。
     *
     * @param body
     * @param token
     * @return
     */
    public static byte getSum(byte[] body, byte[] token) {
        byte result = body[0];
        for (int i = 1; i < body.length; i++) {
            result ^= body[i];
        }
        if (token != null) {
            for (int i = 0; i < token.length; i++) {
                result ^= token[i];
            }
        }
        return result;
    }

    public static double toPercentage(int value) {
        return value * 4 / 100;
    }

    public static void main(String[] args) {
        byte parseByte = Byte.parseByte("-128");
        int unsignedByte = parseUnsignedByte(parseByte);
        System.out.println(parseByte + ":" + unsignedByte);
        System.out.println(parseUnsignedBytes(new byte[]{1}));
        System.out.println(parseUnsignedBytes(new byte[]{0}));
        System.out.println(parseUnsignedBytes(new byte[]{-1}));
        System.out.println(parseUnsignedBytes(new byte[]{-128}));
        System.out.println(parseUnsignedBytes(new byte[]{1, 1}));
        System.out.println(parseUnsignedBytes(new byte[]{0, -1}));
        System.out.println(parseUnsignedBytes(new byte[]{-1, -1}));
        System.out.println(parseUnsignedBytes(new byte[]{0, -1, -1, -1}));
        System.out.println(parseUnsignedBytes(new byte[]{0, 0, -1, -1}));
        System.out.println(parseUnsignedBytes(new byte[]{-1, -1, -1, -1}));
        System.out.println(parseUnsignedBytes(new byte[]{-1, -1, -1, -1, -1}));
        System.out.println(parseUnsignedBytes(new byte[]{-1, -1, -1, -1, -1, -1}));
        System.out.println(parseUnsignedBytes(new byte[]{-1, -1, -1, -1, -1, -1, -1}));
        // 数字超出了long型的最大整数
        System.out.println(parseUnsignedBytes(new byte[]{-1, -1, -1, -1, -1, -1, -1, -1}));

        byte[] body = {-1, -1, -1, -1, -1, -1, -1, -1};
        byte sum = getSum(body, null);
        boolean checkSum = checkSum(body, null, sum);
        byte[] token = {123, -123};
        byte[] body1 = {-1, -1, -1, -1, -1, -1, -1, -1, 123};
        byte sum1 = getSum(body1, token);
        boolean checkSum1 = checkSum(body1, token, sum1);
        System.out.println(checkSum + ":" + checkSum1);
    }
}
