package com.wujt.运算符;

import java.util.Random;

/**
 * @author gow
 * @date 2021/7/29
 */
public class BasicOperationDemo {
    public static void main(String[] args) {


        int bit=5;
        long l = -1L << bit;
        long l1 = ~l;
        System.out.println(l+":"+l1);
        assignmentOperation();

        arithmeticOperation();

        // 一元运算
        int a = 10;
        int x = -a;
        int y = +a;

    }

    /**
     * 基本算术运算符：包括加号（+）、减号（-）、除号（/）、乘号（*）以及模数（%，从整数除法中获得余数）
     */
    private static void arithmeticOperation() {

        // Create a random number generator,
        // seeds with current time by default:
        Random rand = new Random();
        int i, j, k;
        // '%' limits maximum value to 99:
        j = rand.nextInt() % 100;
        k = rand.nextInt() % 100;
        pInt("j", j);
        pInt("k", k);
        i = j + k;
        pInt("j + k", i);
        i = j - k;
        pInt("j - k", i);
        i = k / j;
        pInt("k / j", i);
        i = k * j;
        pInt("k * j", i);
        i = k % j;
        pInt("k % j", i);
        j %= k;
        pInt("j %= k", j);
        // Floating-point number tests:
        float u, v, w;  // applies to doubles, too
        v = rand.nextFloat();
        w = rand.nextFloat();
        pFlt("v", v);
        pFlt("w", w);
        u = v + w;
        pFlt("v + w", u);
        u = v - w;
        pFlt("v - w", u);
        u = v * w;
        pFlt("v * w", u);
        u = v / w;
        pFlt("v / w", u);
        // the following also works for
        // char, byte, short, int, long,
        // and double:
        u += v;
        pFlt("u += v", u);
        u -= v;
        pFlt("u -= v", u);
        u *= v;
        pFlt("u *= v", u);
        u /= v;
        pFlt("u /= v", u);
    }

    /**
     * 赋值运算符： =
     */
    private static void assignmentOperation() {
        int i;
        i = 9;
        String name = "gow";
    }

    // Create a shorthand to save typing:
    static void prt(String s) {
        System.out.println(s);
    }

    // shorthand to print a string and an int:
    static void pInt(String s, int i) {
        prt(s + " = " + i);
    }

    // shorthand to print a string and a float:
    static void pFlt(String s, float f) {
        prt(s + " = " + f);
    }
}
