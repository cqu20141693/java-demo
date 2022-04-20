package com.wujt.函数式编程;

/**
 * 直接接受一个无参任务，并且没有返回结果
 * public interface Runnable
 *
 * @author gow 2021/06/12
 */
public class RunnableDemo {

    public static void main(String[] args) {
        Runnable runnable = () -> System.out.println("hello world");
        runnable.run();
    }

}
