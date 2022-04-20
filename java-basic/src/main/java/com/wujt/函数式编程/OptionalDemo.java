package com.wujt.函数式编程;

import java.util.Optional;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/1/19
 * @description :
 */
public class OptionalDemo {
    public static void main(String[] args) {
        String test1="124";
        Optional.ofNullable(test1).map(i-> {System.out.println(i);return "";}).orElseThrow(()->new RuntimeException());
    }
}
