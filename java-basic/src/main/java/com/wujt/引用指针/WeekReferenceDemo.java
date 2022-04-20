package com.wujt.引用指针;


import com.wujt.函数式编程.User;

import java.lang.ref.WeakReference;

/**
 * 弱引用对于对象的回收是没有影响的；只要对象的强引用为0；GC 可以进行回收
 *
 * @author wujt
 */
public class WeekReferenceDemo {

    static class Product extends WeakReference<Integer> {

        private String name;

        public Product(Integer id, String name) {
            // 构建弱引用对象
            super(id);
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static void main(String[] args) {
        User user = new User();
        user.setName("taoge");
        System.out.println(user.getName());
        WeakReference<User> weakReference = new WeakReference<>(user);
        System.out.println(weakReference.get().getName());
        user = null;
        System.gc();
        System.out.println(weakReference.get());

        // 自定义弱引用对象
        Integer integer = 1000;
        Product product = new Product(integer, "taoge");

        //  获取的是弱应用对象；
        System.out.println(product.get());
        integer = null;
        System.gc();
        System.out.println(product.get());
        System.out.println(product.getName());
    }
}
