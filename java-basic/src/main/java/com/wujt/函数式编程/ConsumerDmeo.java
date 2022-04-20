package com.wujt.函数式编程;

import java.util.function.Consumer;

/**
 * 消费一个输入的参数，进行处理；无输出
 * interface Consumer<T>
 * <p>
 * 函数式API：
 * void accept(T t);
 * 默认API：
 * 当前算子消费完后，接着after进行二次消费
 * default Consumer<T> andThen(Consumer<? super T> after) {
 * Objects.requireNonNull(after);
 * return (T t) -> { accept(t); after.accept(t); };
 * }
 *
 * @author wujt
 */
public class ConsumerDmeo {
    public static void main(String[] args) {
        // 消费数据设置名称
        Consumer<User> consumer = value -> value.setName("taoge");
        Consumer<User> andThen = consumer.andThen(value -> value.setAge(23));
        User user = new User();
        andThen.accept(user);
        System.out.println(user);
    }
}
