package com.wujt.并发编程.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * CompletionService ： 提交异步任务，从队列中拿取并移除第一个元素 (take/poll)
 * // 提交任务
 * Future<V> submit(Callable<V> task);
 * Future<V> submit(Runnable task, V result);
 * <p>
 * 从阻塞队列中获取并移除阻塞队列第一个元素
 * Future<V> take() throws InterruptedException：如果队列为空，那么调用 take() 方法的线程会被阻塞
 * Future<V> poll()： 如果队列为空，那么调用 poll() 方法的线程会返回 null
 * Future<V> poll(long timeout, TimeUnit unit) throws InterruptedException
 * <p>
 * <p>
 * ExecutorCompletionService 是CompletionService的唯一实现类
 * 构造函数
 * public ExecutorCompletionService(Executor executor)
 * public ExecutorCompletionService(Executor executor,BlockingQueue<Future<V>> completionQueue)
 *
 * @author gow 2021/06/12
 */
@Slf4j
public class CompletionServiceDemo {

    private static class Result {
        public Result(Integer id) {
            this.id = id;
        }

        private Integer id;

        @Override
        public String toString() {
            return "Result{" +
                    "id=" + id +
                    '}';
        }
    }

    public static void main(String[] args) throws Exception {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 10, 4, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
        ArrayList<Callable<Result>> callables = new ArrayList<>();
        callables.add(() -> null);
        callables.add(() -> new Result(1));
        callables.add(() -> new Result(2));

        solveAll(threadPoolExecutor, callables);
        log.info("solveFirst");
        solveFirst(threadPoolExecutor, callables);
    }

    /**
     * 假设你有一组针对某个问题的solvers，每个都返回一个类型为Result的值，并且想要并发地运行它们，
     * 处理每个返回一个非空值的结果，在某些方法使用(Result r)
     *
     * @param e
     * @param solvers
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void solveAll(Executor e,
                                 Collection<Callable<Result>> solvers)
            throws Exception {
        CompletionService<Result> ecs
                = new ExecutorCompletionService<Result>(e);
        for (Callable<Result> s : solvers)
            ecs.submit(s);
        int n = solvers.size();
        for (int i = 0; i < n; ++i) {
            Result r = ecs.take().get();
            if (r != null)
                use(r);
        }
    }

    private static void use(Result r) {
        log.info("result={}", r);
    }

    private static void solveFirst(Executor e,
                                   Collection<Callable<Result>> solvers)
            throws InterruptedException {
        CompletionService<Result> ecs
                = new ExecutorCompletionService<Result>(e);
        int n = solvers.size();
        List<Future<Result>> futures
                = new ArrayList<Future<Result>>(n);
        Result result = null;
        try {
            for (Callable<Result> s : solvers)
                futures.add(ecs.submit(s));
            for (int i = 0; i < n; ++i) {
                try {
                    Result r = ecs.take().get();
                    if (r != null) {
                        result = r;
                        break;
                    }
                } catch (ExecutionException ignore) {
                }
            }
        } finally {
            for (Future<Result> f : futures)
                // 注意这里的参数给的是 true，详解同样在前序 Future 源码分析文章中
                f.cancel(true);
        }

        if (result != null)
            use(result);
    }
}
