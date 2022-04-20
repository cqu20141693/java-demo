package com.wujt.函数式编程;

import java.util.function.UnaryOperator;

/**
 * 特俗的单元运算符: 输入和返回值类型相同的操作符
 * UnaryOperator<T> extends Function<T, T>
 *
 * @author wujt
 */
public class UnaryOperatorDemo {
    public static void main(String[] args) {
        String test1 = "string";
        UnaryOperator<String> unaryOperator = String::trim;
        System.out.println(unaryOperator.apply(test1).length());
        test1 = "   test  ";
        System.out.println(unaryOperator.apply(test1).length());
    }
}
