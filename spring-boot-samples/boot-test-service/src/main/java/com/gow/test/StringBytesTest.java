package com.gow.test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author gow
 * @date 2021/10/28
 */
public class StringBytesTest {
    public static void main(String[] args) {
        String bin="101";
        byte[] bytes = bin.getBytes(StandardCharsets.UTF_8);
        byte[] bytes1 = {0, 1, 0, 1};
        String s1 = new String(bytes1);
        byte[] encode = Base64.getEncoder().encode("hello".getBytes(StandardCharsets.UTF_8));
        String src = new String(encode);
        System.out.println(src);
        byte[] decode = Base64.getDecoder().decode(src);


    }
}
