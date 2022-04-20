package com.wujt.函数式编程;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 接受两个参数处理得到一个结果的函数式接口，具有一个返回值
 * interface BiFunction<T, U, R>
 * <p>
 * 函数是API:
 * R apply(T t, U u);
 * 默认实现API：
 * 执行当前运算符后；利用Function after 接口对结果进行二次处理
 * default <V> BiFunction<T, U, V> andThen(Function<? super R, ? extends V> after) {
 * Objects.requireNonNull(after);
 * return (T t, U u) -> after.apply(apply(t, u));
 * }
 *
 * @author wujt
 */
public class BiFunctionDemo {
    public static void main(String[] args) {
        BiFunction<String, String, String> binaryOperator = (first, second) -> first + second;
        String apply = binaryOperator.apply("taoge ", "nihao!");
        System.out.println("apply=" + apply);
        Function<String, Integer> function = source -> Optional.ofNullable(source).map(String::length).orElse(0);
        BiFunction<String, String, Integer> biFunction = binaryOperator.andThen(function);
        Integer gq = biFunction.apply("gq ", " love me");
        System.out.println("gq=" + gq);
    }
}
