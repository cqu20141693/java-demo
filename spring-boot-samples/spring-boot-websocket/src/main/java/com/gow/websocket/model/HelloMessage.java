package com.gow.websocket.model;

/**
 * @author gow
 * @date 2021/7/2 0002
 */
public class HelloMessage {
    private String name;

    public HelloMessage() {
    }

    public HelloMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
