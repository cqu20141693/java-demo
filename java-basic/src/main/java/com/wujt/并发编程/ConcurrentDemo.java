package com.wujt.并发编程;


import com.wujt.函数式编程.User;

import java.util.concurrent.CompletableFuture;

/**
 * @author wujt
 */
public class ConcurrentDemo {
    volatile Boolean flag=false;
   static User user = new User();
    public static void main(String[] args) {
        CompletableFuture<Object> objectCompletableFuture = new CompletableFuture<>();

        synchronized (user) {
            System.out.println("hello");

        }
        System.out.println(user);
    }
}
