package com.wujt.java11;

/**
 * @author gow 2021/06/13
 */
public class StringDemo {
    public static void main(String[] args) {
        // 判断字符串是否为空白
        boolean blank = " ".isBlank();// true
        // 去除首尾空格
        String strip = " Java stack ".strip();// "Java stack"
        // 去除尾部空格
        String stripTrailing = " Java stack ".stripTrailing();// " Java stack"
        // 去除首部空格
        String stripLeading = " Java stack ".stripLeading();// "Java stack "
        // 复制字符串
        String repeat = "Java".repeat(3);// "JavaJavaJava"

        // 行数统计
        long count = "A\nB\nC".lines().count();// 3
    }
}
