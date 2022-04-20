package com.wujt.并发编程.future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 如何设计一个异步回调任务：
 * <p>
 * 自定义一个回调接口：
 * 包括一个需要实现回到执行的任务
 * 自定一个MyFutureTask :
 * List<Object> waiters : 当添加回调时发现任务没有完成；将回调任务放到waiter中
 * <p>
 * <p>
 * 需要一个自定义执行器：
 *
 * @author wujt
 */
public class MyListenerFuture {

    static interface MyListenerTask {
        <T> void complete(T value);
    }

    static class MyListenerFutureTask<T> implements RunnableFuture<T> {

        private Callable task;
        private List<MyListenerTask> waiters;

        public MyListenerFutureTask(Callable task) {
            this.task = task;
            waiters = new ArrayList<>();
        }

        @Override
        public void run() {

        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return false;
        }

        @Override
        public T get() throws InterruptedException, ExecutionException {
            return null;
        }

        @Override
        public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return null;
        }

    }

    static class MyTaskExecutor extends AbstractExecutorService {


        private static class MyFutureThread extends Thread {

        }

        @Override
        protected final <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
            return new MyListenerFutureTask<T>(callable);
        }

        @Override
        public void execute(Runnable command) {

        }

        @Override
        public void shutdown() {

        }

        @Override
        public List<Runnable> shutdownNow() {
            return null;
        }

        @Override
        public boolean isShutdown() {
            return false;
        }

        @Override
        public boolean isTerminated() {
            return false;
        }

        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return false;
        }
    }

    public static void main(String[] args) {

        /**
         *  google ListenerFuture 利用回到线程池进行提交任务；然后在对任务进行封装为
         */
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "callable";
            }
        };
        MyTaskExecutor myTaskExecutor = new MyTaskExecutor();
        myTaskExecutor.submit(callable);
    }
}
