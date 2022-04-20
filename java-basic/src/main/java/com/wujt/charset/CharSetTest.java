package com.wujt.charset;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 理解字符集与字符编码问题
 *
 * 统一的字符集unicode 字符集
 * 编码方式： base64编码，utf-8编码，gbk-2312编码等等
 *
 * @author wujt
 */
public class CharSetTest {
    public static void main(String[] args) throws UnsupportedEncodingException {

        String unicode=new String("iii涛哥，你好");

        System.out.println(Charset.defaultCharset());
        String s = "哈哈涛哥哥";
        char[] chars = unicode.toCharArray();
        byte[] utf8s = unicode.getBytes(StandardCharsets.UTF_8);
        // 模拟得到gbk bytes
        byte[] gbks = unicode.getBytes("gbk");
        // 通过gbk编码方式得到String
        String gbk = new String(gbks, "gbk");
        // 再通过
        byte[] bytes = gbk.getBytes("gbk");
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
