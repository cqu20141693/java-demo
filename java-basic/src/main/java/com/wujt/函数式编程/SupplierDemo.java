package com.wujt.函数式编程;


import java.util.function.Supplier;

/**
 * 没有参数输入，输出一个对象
 * interface Supplier<T>
 * <p>
 * 函数式API：
 * T get();
 *
 * @author wujt
 */
public class SupplierDemo {
    public static void main(String[] args) {

        Supplier<User> supplier = () -> getUser("userName", 23);
        User user = supplier.get();
        System.out.println("user=" + user);
    }

    private static User getUser(String userName, int age) {
        User user = new User();
        user.setName(userName);
        user.setAge(age);
        return user;
    }
}
