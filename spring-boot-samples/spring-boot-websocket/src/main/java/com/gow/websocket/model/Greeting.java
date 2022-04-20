package com.gow.websocket.model;

/**
 * @author gow
 * @date 2021/7/2 0002
 */
public class Greeting {
    private String content;

    public Greeting() {
    }

    public Greeting(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
