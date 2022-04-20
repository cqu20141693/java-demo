package com.wujt.并发编程.thread;

/**
 * Runnable：一个任务的顶级接口，不具备返回值
 *
 * @author wujt
 */
public class RunnableDemo {
    static class MyTask implements Runnable {
        @Override
        public void run() {
            System.out.println("做我自己的工作！");
        }
    }

    public static void main(String[] args) {
        MyTask myTask = new MyTask();
        Thread thread = new Thread(myTask, "myTask");
        thread.start();

    }
}
