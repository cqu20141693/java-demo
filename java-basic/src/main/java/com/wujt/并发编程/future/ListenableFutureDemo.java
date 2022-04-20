package com.wujt.并发编程.future;

import com.google.common.util.concurrent.*;
import com.wujt.函数式编程.User;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 *
 * 实现原理：
 * 自定义一个线程池：ListeningExecutorService 用于对任务的包装执行。
 *定义个FutureTask：TrustedListenableFutureTask 继承了RunnableFuture 和AbstractFuture<V> implements ListenableFuture<V>
 *  Task核心功能： 需要对callable 进行适配，然后自实现run方法；实现同步阻塞get 相关的接口；cancel 接口；需要实现异步响应Listener
 *  的存储和通知； 还需要对同步阻塞队列的存储和通知。
 *
 *  对添加回调函数方法实现：如果任务完成；直接进行回调；如果任务没有完成；加入到等待回到队列中（此处要注意多线程问题）：
 * @author wujt
 */
public class ListenableFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        // 包装后的Task:TrustedListenableFutureTask
        ListenableFuture<User> future = executorService.submit(
                () -> {
                    User user1 = new User();
                    user1.setName("taoge");
                    user1.setAge(23);
                    return user1;
                });
        //
        Futures.addCallback(
                future,
                new FutureCallback<User>() {
                    // we want this handler to run immediately after we push the big red button!
                    public void onSuccess(User user) {
                        System.out.println(user);
                    }

                    public void onFailure(Throwable thrown) {
                        System.out.println("occur exception");
                    }
                },
                executorService);
        User user = future.get();
        ListenableFuture<String> future1 = executorService.submit(() -> {
            return "rowKey";
        });
        // 没有结果的异步响应。
        future1.addListener(() -> {
            System.out.println("OK");
        }, executorService);

    }
}
