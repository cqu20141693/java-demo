package com.wujt.运算符;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/2/19
 * @description : java位运算
 * 若两个输入位都是 1，则按位“与运算符” & 运算后结果是 1，否则结果是 0。若两个输入位里至少有一个是 1，
 * 则按位“或运算符” | 运算后结果是 1；只有在两个输入位都是 0 的情况下，运算结果才是 0。
 * 若两个输入位的某一个是 1，另一个不是 1，那么按位“异或运算符” ^ 运算后结果才是 1。
 * 按位“非运算符” ~ 属于一元运算符；它只对一个自变量进行操作（其他所有运算符都是二元运算符）。
 * 按位非运算后结果与输入位相反。例如输入 0，则输出 1；输入 1，则输出 0。
 */
public class BitOperateDemo {
    public static void main(String[] args) {
        // & ：与运算符 ： 两个输入都为1结果等于1
        // | : 或运算符 ： 其中存在一个输入为1结果等于1
        // ^ : 异或运算符 ： 当且仅当只有一个输入为1结果等于1

        //~ : 非运算符 ：当输入1结果为0，输入0结果为1；
        // 异或结论： A~A=0;
        // 0异或任何数等于其本身

        // 在位运算中不能对boolean值进行非运算， 建议不要对布尔值进行位运算


        testByte();


    }

    private static void testByte() {
        byte[] control = new byte[2];
        control[0] = (byte) 0x83;
        control[1] = (byte) 0xff;
        int nodeLen = (control[0] & 0x03) << 8;
        nodeLen += control[1] & 0xff;
        System.out.println(nodeLen);
        int i=1023;
        control[0]=0;
        control[1]=0;
        control[0] |= (i >> 8);
        control[1] |= i;

        control[0]=-128;
        control[1]=0;
        control[0] |= (i >> 8);
        control[1] |= i;
    }
}
