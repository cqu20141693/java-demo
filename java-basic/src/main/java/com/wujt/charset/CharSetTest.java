package com.wujt.charset;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * @author wujt
 */
public class CharSetTest {
    public static void main(String[] args) throws UnsupportedEncodingException {

        String gbk=new String("iii涛哥");

        System.out.println(Charset.defaultCharset());
        String s = "哈哈涛哥哥";

        testGBKToUTF8(s);
        testGBKToUTF8("涛哥");
    }

    private static void testGBKToUTF8(String s) throws UnsupportedEncodingException {
        byte[] b3 = s.getBytes("UTF-8");
        byte[] b5 = s.getBytes("gbk");

        String s1 = new String(b5);
        String gbk1 = new String(s1.getBytes(), "gbk");
        System.out.println(b3.length);
        System.out.format("%X %X %X\n", b3[0],b3[1],b3[2]);
        System.out.println(new String(b3));

    }
}
