package com.wujt.并发编程.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Callable ： 一个具有返回值的任务接口
 * 主要利用FutureTask 实现对任务的执行和结果的响应。
 * 原理{@link com.wujt.并发编程.future.FutureTaskDemo}
 *
 * @author wujt
 */
public class CallableDemo {
    public static void main(String[] args) {
        Callable<String> callable = () -> {
            System.out.println("我是一个Callable 对象");
            return "callable";
        };
        FutureTask<String> futureTask = new FutureTask<>(callable);

        Thread thread = new Thread(futureTask);
        thread.start();
        try {
            System.out.println("任务结果：" + futureTask.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }
}
