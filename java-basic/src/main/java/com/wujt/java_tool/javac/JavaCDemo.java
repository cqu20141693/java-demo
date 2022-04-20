package com.wujt.java_tool.javac;

/**
 * javac command
 * <p>
 * Directly compile the source file to the source file directory
 * javac com/wujt/java_tool/javac/JavaCDemo.java
 * <p>
 * Compile the source file to the specified directory
 * javac -d D:\working\cqu20141693\2020\my-demo\java-basic-demos\target\classes com/wujt/java_tool/javac/*.java
 *
 * @author wujt
 */
public class JavaCDemo {
    public static void main(String[] args) {
        if (args.length > 0) {
            System.out.println(args[0]);
        }

        User user = new User();
        System.out.println(user.getName());
    }
}
