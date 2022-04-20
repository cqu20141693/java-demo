package com.wujt.java_tool.javac;

/**
 * @author wujt
 */

public class User {
    private String name;

    public synchronized void updateName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
