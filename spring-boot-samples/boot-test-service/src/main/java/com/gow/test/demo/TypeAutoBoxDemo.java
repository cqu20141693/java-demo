package com.gow.test.demo;

/**
 * @author gow
 * @date 2021/7/21
 */
public class TypeAutoBoxDemo {
    public static void main(String[] args) {
        Integer integer=null;
        extracted(integer);
    }

    private static void extracted(int age) {
        System.out.println(age);
    }
}
