package com.wujt.函数式编程;

import java.util.Optional;
import java.util.function.Function;

/**
 * 单元函数式接口，接收一个输入参数，产生一个输出
 * interface Function<T, R>
 * <p>
 * 函数式API：
 * R apply(T t);
 * <p>
 * 默认API：
 * 在before计算后对其结果之后进行计算
 * default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
 * Objects.requireNonNull(before);
 * return (V v) -> apply(before.apply(v));
 * }
 * <p>
 * 先执行当前算子，然后在利用after接收当前计算结果进行二次计算；
 * default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
 * Objects.requireNonNull(after);
 * return (T t) -> after.apply(apply(t));
 * }
 * <p>
 * 获取当前函数式接口的输入参数
 * identity()
 *
 * @author wujt
 */
public class FunctionDemo {
    public static void main(String[] args) {

        Function<String, Integer> function = source -> Optional.ofNullable(source).map(String::length).orElse(0);

        Integer taoge = function.apply("taoge");
        System.out.println("gq=" + taoge);
        Function<String, Long> andThen = function.andThen(FunctionDemo::feblaq);
        Long apply = andThen.apply("taoge");
        System.out.println("andThen=" + apply);
    }

    public static Long feblaq(Integer i) {
        Long num = Long.valueOf(i);
        if (num < 2) {
            return num;
        } else {
            Long first = 1L, second = 2L, sum = 0L;
            int cnt = 1;
            while (cnt < i) {
                sum = first + second;
                first = second;
                second = sum;
                cnt++;
            }
            return sum;
        }
    }
}
