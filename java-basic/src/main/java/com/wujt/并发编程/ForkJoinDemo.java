package com.wujt.并发编程;

import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * Fork
 * Fork is a process in which a task splits itself into smaller and independent sub-tasks
 * which can be executed concurrently.
 * <p>
 * Join
 * Join is a process in which a task join all the results of sub-tasks
 * once the subtasks have finished executing, otherwise it keeps waiting.
 * <p>
 * ForkJoinPool
 * it is a special thread pool designed to work with fork-and-join task splitting.
 * <p>
 * RecursiveAction
 * RecursiveAction represents a task which does not return any value.
 * <p>
 * RecursiveTask
 * RecursiveTask represents a task which returns a value.
 * <p>
 * Fork
 * <p>
 * Join
 *
 * @author wujt
 */
public class ForkJoinDemo {
    /**
     * ForkJoinPool since 1.7
     * <p>
     * 原理： 通过 fork 实现将任务分发到各个队里中，然后使用工厂创建线程执行任务
     *        利用工作窃取技术实现当某个线程将自己的任务完成后可以去协助完成其他线程的任务队列中的任务
     *
     * <p>
     * API:
     *  compute 直接当前线程进行处理任务
     *  fork(): 将任务放到线程的任务队列中；
     *  join
     */
    private static ForkJoinPool forkJoinPool = new ForkJoinPool(8);

    public static void main(String[] args) {
        int nThreads = Runtime.getRuntime().availableProcessors();
        System.out.println(nThreads);

        int[] numbers = new int[1000];
        IntStream.range(0, 1000).parallel().forEach(i -> numbers[i] = i);

        Sum task = new Sum(numbers, 0, numbers.length);
        //task.fork();
        ForkJoinTask<Long> submit1 = forkJoinPool.submit(task);
        try {
            System.out.println(submit1.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName());
        Writer writer = new Writer(0, 64);
        ForkJoinTask<Void> submit = forkJoinPool.submit(writer);

        if (submit.isCompletedAbnormally()) {
            System.out.println(submit.getException().toString());
        }
        try {
            System.out.println(submit.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    static class Sum extends RecursiveTask<Long> {
        private static final int THRESHOLD = 10;
        int low;
        int high;
        int[] array;

        Sum(int[] array, int low, int high) {
            this.array = array;
            this.low = low;
            this.high = high;
        }

        @Override
        protected Long compute() {
            //如果任务足够小开始进行完成
            boolean canComplete = high - low <= THRESHOLD;
            if (canComplete) {
                long sum = 0;
                //  System.out.println(Thread.currentThread().getName());
                for (int i = low; i < high; ++i) {
                    sum += array[i];
                }
                return sum;
            } else {
                // 如果大于阈值就进行拆分
                int mid = low + (high - low) / 2;
                Sum left = new Sum(array, low, mid);
                Sum right = new Sum(array, mid, high);
                // 将子任务拆分出去
                left.fork();
                //第二个任务直接使用当前线程计算而不再开启新的线程。
                long rightResult = right.compute();
                //读取第一个子任务的结果，如果没有完成则等待。
                long leftResult = left.join();
                //合并两个子任务的计算结果
                return leftResult + rightResult;
            }
        }
    }

    /**
     * 打印任务fork,join 过程
     */
    static class Writer extends RecursiveAction {
        private static final int THRESHOLD = 10;
        private int start;
        private int end;

        Writer(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            //如果任务足够小开始进行完成
            boolean canComplete = end - start <= THRESHOLD;
            if (canComplete) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Thread.currentThread().getName());
                for (int i = start; i < end; ++i) {
                    stringBuilder.append(":").append(i);
                }
                System.out.println(stringBuilder.toString());

            } else {
                // 如果大于阈值就进行拆分
                int mid = (end + start) / 2;
                Writer left = new Writer(start, mid);
                Writer right = new Writer(mid, end);
                // 将子任务拆分出去
                left.fork();
                right.fork();

                // 1. join 方法会对当前任务进行完成；从而需要将任务放到一个线程对应的任务队列中（如果初次应该是会从线程工厂中创建线程放入池中）
                // 2. join()方法会执行任务，调用compute()方法；在compute方法中实现对任务的队列继续分发；
                left.join();
                right.join();
            }
        }
    }
}
