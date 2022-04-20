package com.wujt.java9;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * java 9 中, 添加了三个方法来改进它的功能： stream(),ifPresentOrElse(),or()
 * <p>
 * public Stream<T> stream()
 * <p>
 * public void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction)
 * <p>
 * public Optional<T> or(Supplier<? extends Optional<? extends T>> supplier)
 *
 * @author gow 2021/06/13
 */
public class OptionalDemo {

    public static void main(String[] args) {
        streamTest();

        ifPresentOrElseTest();

        orTest();
    }

    /**
     * 如果值存在，返回 Optional 指定的值，否则返回一个预设的值。
     */
    private static void orTest() {
        // 当获取缓存为空时，可以通过数据查询
        Optional<String> optional1 = Optional.of("Mahesh");
        Supplier<Optional<String>> supplierString = () -> Optional.of("Not Present");
        optional1 = optional1.or(supplierString);
        optional1.ifPresent(x -> System.out.println("Value: " + x));
        optional1 = Optional.empty();
        optional1 = optional1.or(supplierString);
        optional1.ifPresent(x -> System.out.println("Value: " + x));

        // java 8  empty 情况下不能执行操作
        Optional.empty().ifPresent(System.out::println);

    }

    /**
     * 接受两个参数 Consumer 和 Runnable
     * 如果一个 Optional 包含值，则对其包含的值调用函数 action，即 action.accept(value)，这与 ifPresent 一致；
     * 如果 Optional 不包含值，那么 ifPresentOrElse 便会调用 emptyAction
     */
    private static void ifPresentOrElseTest() {
        Optional<Integer> optional = Optional.of(1);

        optional.ifPresentOrElse(x -> System.out.println("Value: " + x), () ->
                System.out.println("Not Present."));

        optional = Optional.empty();

        optional.ifPresentOrElse(x -> System.out.println("Value: " + x), () ->
                System.out.println("Not Present."));
    }

    /**
     * stream 方法：将 Optional 转为一个 Stream，如果该 Optional 中包含值，那么就返回包含这个值的 Stream，
     * 否则返回一个空的 Stream（Stream.empty()）
     */
    private static void streamTest() {
        List<Optional<String>> list = Arrays.asList(
                Optional.empty(),
                Optional.of("A"),
                Optional.empty(),
                Optional.of("B"));

        //filter the list based to print non-empty values

        //if optional is non-empty, get the value in stream, otherwise return empty
        List<String> filteredList = list.stream()
                .flatMap(o -> o.isPresent() ? Stream.of(o.get()) : Stream.empty())
                .collect(Collectors.toList());

        //Optional::stream method will return a stream of either one
        //or zero element if data is present or not.
        List<String> filteredListJava9 = list.stream()
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        System.out.println(filteredList);
        System.out.println(filteredListJava9);

        Optional.empty().stream().forEach(System.out::println);
        Optional.of("hello").stream().forEach(System.out::println);
        Optional.ofNullable(null).stream().forEach(System.out::println);
        Optional.of(List.of("hello", "world")).stream().forEach(System.out::println);

    }
}
