package com.wujt.运算符;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/2/19
 * @description : java 位移运算符
 *
 * 左移位运算符（<<）能将运算符左边的运算对象向左移动运算符右侧指定的位数（在低位补0）
 * “有符号”右移位运算符（>>）则将运算符左边的运算对象向右移动运算符右侧指定的位数
 * “有符号”右移位运算符使用了“符号扩展”：若值为正，则在高位插入0；若值为负，则在高位插入1
 * “无符号”右移位运算符（>>>），它使用了“零扩展”：无论正负，都在高位插入0
 *
 */
public class ShiftOperateDemo {
    public static void main(String[] args) {
        test(-1);
        test(-8);
        int i = 1;
        test(i);
        test(8);

        Integer var = 1;


        Integer var2 = 2;
        var2 = var2 | var;
        var2 = var2 | var;
        var = var << 31;
        System.out.println(var);
    }

    private static void test(int i) {
        System.out.println(i);
        System.out.println(Integer.toBinaryString(i));
        System.out.println(i >> 1);
        System.out.println(Integer.toBinaryString(i >> 1));
        System.out.println(i << 1);
        System.out.println(Integer.toBinaryString(i << 1));
        System.out.println(i >>> 1);
        System.out.println(Integer.toBinaryString(i >>> 1));
    }
}
