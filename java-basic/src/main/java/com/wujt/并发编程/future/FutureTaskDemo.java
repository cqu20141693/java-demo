package com.wujt.并发编程.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * FutureTask: 一个可以获取返回值的任务线程类；不具备异步回调能力。
 * 具备Runnable run 能力，可以组装到线程类，具备Future get,cancel,isDone 能力
 *
 * <p>
 * 利用了代理模式实现对Callable 接口任务的运行，
 * 利用适配器模式适配到Runnable 接口；利用线程进行执行。
 * <p>
 * fields:
 * private Callable<V> callable; ： 任务对象
 * private Object outcome; ： 任务结果
 * private volatile WaitNode waiters : get 阻塞等待线程队列
 *
 * <p>
 * API：
 * public boolean cancel(boolean mayInterruptIfRunning)  ： 取消任务执行；
 * public V get() 获取任务执行结果
 * <p>
 * <p>
 * <p>
 * 实现原理：
 * 当FutureTask 对象被线程执行后；会调用器run()方法；方法中会调用依赖Callable callable.call()方法
 * 获取返回值； 然后进行状态切换为完成，在将结果值设置到FutureTask 中；再切换状态为Normal;然后再进行waiters 唤醒
 * <p>
 * 当其他线程调用FutureTask 的get方法时；首先是判断任务是否完成；完成直接获取FutureTask中的结果；
 * 否则进行阻塞等待；在阻塞等待过程中做了优化：如果状态为完成；就放弃执行权限，然后进行循环；如果大于完成状态；可以直接返回了
 * 如果还没完成；则将当前线程放入等待队列中waiters; 并进行阻塞，等待任务完成后进行唤醒执行。
 *
 * 通过Future已经可以获取到现超执行的结果，但是仍有以下的不足：
 * 1. 调用 get() 方法会阻塞程序
 * 2. 不能链式执行
 * 3. 整合多个 Future 执行结果方式笨重,在任务全部执行完成之后做后续操作
 *
 *
 * @author wujt
 */
public class FutureTaskDemo {
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
