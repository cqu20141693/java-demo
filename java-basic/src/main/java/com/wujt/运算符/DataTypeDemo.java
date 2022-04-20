package com.wujt.运算符;

/**
 * @author wujt
 */
public class DataTypeDemo {
    public static void main(String[] args) {
        /**
         * java 中对自动装箱的-128-127 的整数进行了缓存；每次使用返回的是同一个缓存引用
         */
        Integer i=127;
        Integer j=127;
        System.out.println(i==j);

        /**
         * 字符串
         */
        String s1="taoge";
        String s2="taoge";
        System.out.println(s1==s2);
        String s3=new String("taoge");
        System.out.println(s1==s3);
        System.out.println(s1.equals(s3));



    }
}
