package com.wujt.java10;

import java.util.List;

/**
 * 局部变量类型推荐仅限于如下使用场景：
 * 局部变量初始化
 * for循环内部索引变量
 * 传统的for循环声明变量
 * <p>
 * Java官方表示，它不能用于以下几个地方：
 * 方法参数
 * 构造函数参数
 * 方法返回类型
 * 字段
 * 捕获表达式（或任何其他类型的变量声明）
 *
 * @author gow 2021/06/13
 */
public class VarDemo {

    public static void main(String[] args) {
        // 变量定义和初始化
        var name = getName();
        //for
        List<String> books = List.of("java", "go");
        for (var book : books) {
        }

        for (var i = 0; i < 10; i++) {
        }


    }

    private static String getName() {
        return "Alex";
    }
}
