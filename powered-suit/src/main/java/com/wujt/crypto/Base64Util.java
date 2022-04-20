package com.wujt.crypto;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

/**
 * 参考： https://juejin.cn/post/6844903807285985288
 *
 * @author wujt
 */
public class Base64Util {

    public static String encodeToString(String msg) {
        return encodeToString(msg.getBytes(StandardCharsets.UTF_8));
    }

    public static String encodeToString(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] decode(String decode) {
        return Base64.getDecoder().decode(decode);
    }

    public static void main(String args[]) {
        Charset charsetName = StandardCharsets.UTF_8;

        // 使用基本编码
        String base64encodedString = Base64.getEncoder().encodeToString("runoob?java8".getBytes(charsetName));
        System.out.println("Base64 编码字符串 (基本) :" + base64encodedString);

        // 解码
        byte[] base64decodedBytes = Base64.getDecoder().decode(base64encodedString);

        System.out.println("原始字符串: " + new String(base64decodedBytes, charsetName));

        String urlData = "runoob/java8/+";
        String encodeToString = Base64.getUrlEncoder().encodeToString(urlData.getBytes(charsetName));
        System.out.println("Base64 编码字符串 (URL) :" + encodeToString);

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 10; ++i) {
            stringBuilder.append(UUID.randomUUID().toString());
        }

        byte[] mimeBytes = stringBuilder.toString().getBytes(charsetName);
        String mimeEncodedString = Base64.getMimeEncoder().encodeToString(mimeBytes);
        System.out.println("Base64 编码字符串 (MIME) :" + mimeEncodedString);


        String base64Binary = DatatypeConverter.printBase64Binary("secret".getBytes());
        String refreshSecret = DatatypeConverter.printBase64Binary("refreshSecret".getBytes());
        byte[] bytes = DatatypeConverter.parseBase64Binary(base64Binary);
        byte[] refreshs = DatatypeConverter.parseBase64Binary(refreshSecret);
        System.out.println(new String(bytes));
    }
}
