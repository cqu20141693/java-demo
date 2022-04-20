package com.gow.codec.base;

/**
 * @author gow
 * @date 2021/7/28
 */
public class TypeConvertTest {

    public static void main(String[] args) {

        floatTest();
        intTest();
        longTest();
    }

    private static void longTest() {

        TypeLongConvert typeLongConvert = new TypeLongConvert();
        byte[] bytes = typeLongConvert.convertToBytes(1L);
        ConvertResponse<Long> response = typeLongConvert.rawDataConvert(bytes);
        byte[] bytes1 = typeLongConvert.convertToBytes(-1L);
        byte[] bytes2 = typeLongConvert.convertToBytes(Long.MAX_VALUE);
        byte[] bytes3 = typeLongConvert.convertToBytes(Long.MIN_VALUE);
        ConvertResponse<Long> response1 = typeLongConvert.rawDataConvert(bytes1);
        ConvertResponse<Long> response2 = typeLongConvert.rawDataConvert(bytes2);
        ConvertResponse<Long> response3 = typeLongConvert.rawDataConvert(bytes3);
        byte[] bytes4 = new byte[1];
        bytes4[0] = 127;
        byte[] bytes5 = new byte[2];
        bytes5[0] = 127;
        bytes5[1] = 127;

        byte[] bytes6 = new byte[3];
        bytes6[0] = 127;
        bytes6[1] = 127;
        bytes6[2] = 127;
        byte[] bytes7 = new byte[4];
        bytes7[0] = 127;
        bytes7[1] = 127;
        bytes7[2] = 127;
        bytes7[3] = 127;
        byte[] bytes8 = new byte[5];
        bytes8[0] = 127;
        bytes8[1] = 127;
        bytes8[2] = 127;
        bytes8[3] = 127;
        bytes8[4] = 127;
        byte[] bytes9 = new byte[6];
        bytes9[0] = 127;
        bytes9[1] = 127;
        bytes9[2] = 127;
        bytes9[3] = 127;
        bytes9[4] = 127;
        bytes9[5] = 127;
        byte[] bytes10 = new byte[7];
        bytes10[0] = 127;
        bytes10[1] = 127;
        bytes10[2] = 127;
        bytes10[3] = 127;
        bytes10[4] = 127;
        bytes10[5] = 127;
        bytes10[6] = 127;
        ConvertResponse<Long> response4 = typeLongConvert.rawDataConvert(bytes4);
        ConvertResponse<Long> response5 = typeLongConvert.rawDataConvert(bytes5);
        ConvertResponse<Long> response6 = typeLongConvert.rawDataConvert(bytes6);
        ConvertResponse<Long> response7 = typeLongConvert.rawDataConvert(bytes7);
        ConvertResponse<Long> response8 = typeLongConvert.rawDataConvert(bytes8);
        ConvertResponse<Long> response9 = typeLongConvert.rawDataConvert(bytes9);
        ConvertResponse<Long> response10 = typeLongConvert.rawDataConvert(bytes10);

        byte[] bytes11 = typeLongConvert.convertToBytes(response4.getConvertResult());
        byte[] bytes12 = typeLongConvert.convertToBytes(response5.getConvertResult());
        byte[] bytes13 = typeLongConvert.convertToBytes(response6.getConvertResult());
        byte[] bytes14 = typeLongConvert.convertToBytes(response7.getConvertResult());
        byte[] bytes15 = typeLongConvert.convertToBytes(response8.getConvertResult());
        byte[] bytes16 = typeLongConvert.convertToBytes(response9.getConvertResult());
        byte[] bytes17 = typeLongConvert.convertToBytes(response10.getConvertResult());
        System.out.println("long success");
    }

    private static void intTest() {
        TypeIntConvert intConvert = new TypeIntConvert();
        byte[] bytes = intConvert.convertToBytes(1);
        byte[] bytes1 = new byte[1];
        bytes1[0] = 1;
        byte[] bytes2 = new byte[2];
        bytes2[0] = 1;
        bytes2[1] = 127;
        byte[] bytes3 = new byte[3];
        bytes3[0] = 1;
        bytes3[1] = 127;
        bytes3[2] = 127;
        ConvertResponse<Integer> integerConvertResponse = intConvert.rawDataConvert(bytes);
        ConvertResponse<Integer> response1 = intConvert.rawDataConvert(bytes1);
        ConvertResponse<Integer> response2 = intConvert.rawDataConvert(bytes2);
        ConvertResponse<Integer> response3 = intConvert.rawDataConvert(bytes3);

        // 负数测试
        byte[] bytes4 = intConvert.convertToBytes(-1);
        byte[] bytes5 = intConvert.convertToBytes(Integer.MIN_VALUE);

        ConvertResponse<Integer> response4 = intConvert.rawDataConvert(bytes4);
        ConvertResponse<Integer> response5 = intConvert.rawDataConvert(bytes5);

        System.out.println("success");

    }

    private static void floatTest() {
        TypeFloatConvert typeFloatConvert = new TypeFloatConvert();
        byte[] bytes = typeFloatConvert.convertToBytes(1.0f);
        ConvertResponse<Float> floatConvertResponse = typeFloatConvert.rawDataConvert(bytes);
        Float result = floatConvertResponse.getConvertResult();
    }
}
