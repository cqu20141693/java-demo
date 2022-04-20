package com.wujt.java9;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * java 9 为 Stream 新增了几个方法：dropWhile、takeWhile、ofNullable，为 iterate 方法新增了一个重载方法。
 * <p>
 * default Stream<T> takeWhile(Predicate<? super T> predicate)
 * <p>
 * default Stream<T> dropWhile(Predicate<? super T> predicate)
 * <p>
 * static <T> Stream<T> ofNullable(T t)
 * <p>
 * static <T> Stream<T> iterate(T seed, Predicate<? super T> hasNext, UnaryOperator<T> next)
 *
 * @author gow 2021/06/13
 */
public class StreamDemo {
    public static void main(String[] args) {
        //takeWhile() 方法使用一个断言作为参数，返回给定 Stream 的子集直到断言语句第一次返回 false。
        // 如果第一个值不满足断言条件，将返回一个空的 Stream。
        Stream.of("a", "b", "c", "", "e", "f").takeWhile(s -> !s.isEmpty())
                .forEach(System.out::print);

        //使用一个断言作为参数，直到断言语句第一次返回 false 才返回给定 Stream 的子集
        Stream.of("a", "b", "c", "", "e", "f").dropWhile(s -> !s.isEmpty())
                .forEach(System.out::print);

        //ofNullable 方法可以预防 NullPointerExceptions 异常， 可以通过检查流来避免 null 值
        //元素为 null 则返回一个空流。
        long count = Stream.ofNullable(100).count();
        System.out.println(count);

        count = Stream.ofNullable(null).count();
        System.out.println(count);

        //方法允许使用初始种子值创建顺序（可能是无限）流，并迭代应用指定的下一个方法。
        // 当指定的 hasNext 的 predicate 返回 false 时，迭代停止。
        IntStream.iterate(3, x -> x < 10, x -> x + 3).forEach(System.out::println);
    }
}
