package com.wujt.函数式编程;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * 二元操作函数: 特殊的BiFunction
 * 接收两个参数，返回一个数据
 * @author wujt
 */
public class BinaryOperatorDemo {
    public static void main(String[] args) {
        BinaryOperator<String> binaryOperator = (first, second) -> first + second;
        String apply = binaryOperator.apply("taoge ", "nihao!");
        System.out.println("apply=" + apply);
        Function<String, Integer> function = source -> Optional.ofNullable(source).map(String::length).orElse(0);
        BiFunction<String, String, Integer> biFunction = binaryOperator.andThen(function);
        Integer gq = biFunction.apply("gq ", " love me");
        System.out.println("gq=" + gq);
    }
}
