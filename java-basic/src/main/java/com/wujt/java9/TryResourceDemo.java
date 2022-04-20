package com.wujt.java9;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * java 7 所有实现了 java.lang.AutoCloseable 接口（其中，实现接口 java.io.Closeable 的所有对象），可以使用作为资源
 * 在try中定义资源变量进行使用，try-with-resources 语句确保了每个资源在语句结束时关闭
 *
 * JDK 9 如果你已经有一个资源是 final 或等效于 final 变量,您可以在 try-with-resources 语句中使用该变量，
 * 而无需在 try-with-resources 语句中声明一个新变量,try-with-resources将确保每个资源在语句结束时关闭
 *
 * @author gow 2021/06/13
 */
public class TryResourceDemo {

    public static void main(String[] args) throws IOException {
        System.out.println(readData("test"));
    }

    /**
     * @param message
     * @return
     * @throws IOException
     */
    static String readData(String message) throws IOException {
        Reader inputString = new StringReader(message);
        BufferedReader br = new BufferedReader(inputString);
        try (br) {
            return br.readLine();
        }
    }
}
