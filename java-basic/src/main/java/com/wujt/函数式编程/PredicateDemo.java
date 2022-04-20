package com.wujt.函数式编程;


import java.util.function.Predicate;

/**
 * 接收一个输入参数，返回一个预测Boolean值；
 * interface Predicate<T>
 * <p>
 * 函数式API：
 * boolean test(T t);
 * <p>
 * 默认API：
 * 接收一个predicte 参数；将自身的断言和接收的断言求&&
 * default Predicate<T> and(Predicate<? super T> other) {
 * Objects.requireNonNull(other);
 * return (t) -> test(t) && other.test(t);
 * }
 * <p>
 * 直接对本身的断言求！
 * default Predicate<T> negate() {
 * return (t) -> !test(t);
 * }
 * <p>
 * 本身断言和输入的Predicte 的断言求 ||
 * default Predicate<T> or(Predicate<? super T> other) {
 * Objects.requireNonNull(other);
 * return (t) -> test(t) || other.test(t);
 * }
 * <p>
 * 判断两个预测值是否相等
 * static <T> Predicate<T> isEqual(Object targetRef) {
 * return (null == targetRef)
 * ? Objects::isNull
 * : object -> targetRef.equals(object);
 * }
 * <p>
 * 使用场景：
 * 用于断言
 * 用于过滤实现
 *
 * @author wujt
 */
public class PredicateDemo {
    static Integer defaultAge = 23;
    static Integer maxAge = 40;

    public static void main(String[] args) {
        // 判断用户的年龄是否大于23
        User user = new User();
        user.setAge(24);
        user.setName("taoge");
        Predicate<User> predicate = object -> object != null && (object.getAge() != null && object.getAge() > defaultAge);
        boolean test = predicate.test(user);
        if (test) {
            System.out.println("user's age is greater " + defaultAge);
        }
        Predicate<User> and = predicate.and(object -> object.getAge() < maxAge);
        test = and.test(user);
        if (test) {
            System.out.println("user's age is greater than " + defaultAge + " and less than " + maxAge);
        }
        Predicate<User> predicate1 = object -> object == null || object.getName() == null;
        Predicate<User> negate = predicate1.negate();
        if (negate.test(user)) {
            System.out.println("user's name is not null and name is " + user.getName());
        }

        Predicate<Object> equal = Predicate.isEqual(user);
        User user1 = new User();
        user1.setName(user.getName());
        user1.setAge(user.getAge());
        boolean eq = equal.test(user1);
        if (eq) {
            System.out.println(String.format("user=%s  equals user1=%s", user, user1));
        }
    }
}
