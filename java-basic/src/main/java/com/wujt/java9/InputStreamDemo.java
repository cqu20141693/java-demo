package com.wujt.java9;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author gow 2021/06/13
 */
public class InputStreamDemo {

    public static void main(String[] args) throws IOException {
        var classLoader = InputStreamDemo.class.getClassLoader();
        var file = new File("E:\\java-project\\java-demo\\java-basic\\src\\main\\resources\\javastack.txt");
        FileInputStream inputStream = new FileInputStream(file);
        var javastack = File.createTempFile("java stack2", "txt");
        var outputStream = new FileOutputStream(javastack);
        try (outputStream) {
            inputStream.transferTo(outputStream);
        }
    }
}
