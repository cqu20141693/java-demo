package com.wujt.math;

import java.util.Random;

import static java.lang.Math.*;

/**
 * static long round(double a) 四舍五入
 * static double abs(double a) || 绝对值
 * <p>
 * static double ceil(double a)   向上取整
 * static double floor(double a)   向下取整
 * <p>
 * static double sqrt(double a)  平方根
 * static double pow(double a, double b) a^b
 * <p>
 * static double max(double a, double b)
 * static double min(double a, double b)
 *
 * @author wujt
 */
public class MathDemo {
    public static void main(String[] args) {
        testMath();

        testRandom();
    }

    private static void testRandom() {
        Random random = new Random();
        // 上线
        System.out.println(random.nextInt(5));
    }

    private static void testMath() {
        // 四舍五入
        long round = round(-1.5);
        System.out.println(round);
        round = round(1.5);
        System.out.println(round);
        round = round(1.4);
        System.out.println(round);


        // 绝对值
        double abs = abs(1.5);
        System.out.println(abs);
        abs = abs(-1.5);
        System.out.println(abs);
        //向上取整
        double ceil = ceil(1.5);
        System.out.println(ceil);
        ceil = ceil(-1.5);
        System.out.println(ceil);
        //向下取整
        double floor = floor(1.9);
        System.out.println(floor);
        floor = floor(1.1);
        System.out.println(floor);
    }
}
