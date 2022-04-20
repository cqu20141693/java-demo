package com.wujt.并发编程;


import com.wujt.函数式编程.User;

import java.util.concurrent.Exchanger;

/**
 * @author wujt
 */
public class ExchangerDemo {
    public static void main(String[] args) {
        Exchanger<User> exchanger = new Exchanger<>();
    }
}
