package com.wujt.并发编程.future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wujt
 */
public class MyPromiseDemo {
    private static interface MyListener<T> {
        void complete(T value);
    }

    private static class MyPromise<T> {

        ReentrantLock lock = new ReentrantLock();
        List<MyListener<T>> witers = new ArrayList<>();

        public void addListenter(MyListener<T> listener) {

        }
    }

    /**
     *
     *
     * 继承FutureTask : 可以协助我们实现
     * @param <T>
     */
    private static class MyFutureTask<T> extends FutureTask<T> {

        private MyPromise<T> myPromise;

        public MyFutureTask(Callable<T> callable, MyPromise myPromise) {
            super(callable);
            this.myPromise = myPromise;
        }

        @Override
        public void run() {
            super.run();
        }

        @Override
        protected void done() {
            // 需要处理线程中断，取消任务，和完成任务三种情况。

        }
    }

    public static void main(String[] args) {

    }
}
