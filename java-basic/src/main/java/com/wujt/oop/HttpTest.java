package com.wujt.oop;

/**
 * wcc 2022/5/15
 */
public class HttpTest {

    public static void main(String[] args) {
        // new className(参数)
        Http http = new Http("baidu.com", "qqq");
        http.send();
        System.out.println("get Http field=" + http.getUrl());
    }
}
