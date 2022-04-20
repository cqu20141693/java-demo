package com.wujt.java_tool.javap;


import com.wujt.java_tool.javac.User;

/**
 * javap tool
 *
 * javap -v -l com/wujt/java_tool/javap/JavaPDemo
 *
 * @author wujt
 */
public class JavaPDemo {
    public static void main(String[] args) {
        User user = new User();
        user.updateName("taoge");
        System.out.println(user.getName());
        synchronized (user) {
            user.setName("wujt");
        }
        System.out.println(user.getName());
    }
}
