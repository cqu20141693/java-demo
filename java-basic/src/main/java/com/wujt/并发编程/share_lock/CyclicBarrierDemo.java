package com.wujt.并发编程.share_lock;

import com.wujt.并发编程.thread.MyThreadFactory;
import com.wujt.并发编程.util.ConcurrentUtils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * CyclicBarrier: 共享锁
 * <p>
 * field:
 * The lock for guarding barrier entry
 * private final ReentrantLock lock=new ReentrantLock(); ：lock,保证parties,count，barrierCommand，generation操作的原子性
 * Condition to wait on until tripped
 * private final Condition trip=lock.newCondition(); ： 实现线程间通信，当调用await() API 时，如果parties 不满足，阻塞，否则唤醒所有等待线程。
 * The number of parties
 * private final int parties ： 表示需要等待的
 * The command to run when tripped
 * private final Runnable barrierCommand ： trip 发生执行的动作；
 * The current generation
 * private Generation generation=new Generation() ： 表示一个屏障时期
 * private int count; ： 当前等待的数量。
 * API:
 * public int await()
 * public boolean isBroken()
 * public void reset()；
 *
 * <p>
 * 实现原理：
 * 内部使用独占锁和条件锁实现对共享资源的原子性操作，实现线程通信，协同工作， 当CyclicBarrier 对象cyclicBarrier调用await() 方法；首先会判断当前--count是否为0；
 * 如果是；就进行下一个时期： 利用Condition唤醒所有等待的线程；重置count,generation; 如果不是；就进入线程阻塞操作中；如果不是超时获取，直接阻塞；否则超时堵塞；
 * 如果线程被唤醒；查看当前时期是否被破坏，如果被破坏，直接抛出异常
 * <p>
 * 如果存在任意一个线程被中断，将会导致整个时期被破坏；所有的线程都会抛出BrokenBarrierException异常。
 * <p>
 * 使用场景：
 * 控制多个线程进行并发操作；并且可以重复使用。当到达屏障的时候会进行全部唤醒；并重置屏障值。
 *
 * @author wujt
 */
public class CyclicBarrierDemo {
    static int permit = 5;
    static CyclicBarrier cyclicBarrier = new CyclicBarrier(permit, CyclicBarrierDemo::action);
    static ExecutorService executorService = new ThreadPoolExecutor(5, 5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new MyThreadFactory(new AtomicInteger(0), "CyclicBarrier"));

    private static void action() {
        // cyclicBarrier.reset();
        System.out.println("我们都准备好了：可以开始了");
    }

    public static void main(String[] args) {
        /**
         * 正常测试CyclicBarrier API，利用线程池进行线程资源的重复使用；然后可以通过循环实现对CyclicBarrier的重用。
         */
        testAPI();

        ConcurrentUtils.sleep(1000);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        /**
         * 测试中断情况
         */
        testInterrupte();
        // 让监控线程多活两秒
        ConcurrentUtils.sleep(1000);
    }

    private static void testInterrupte() {
        System.out.println("又有任务来了。");
        try {
            Thread monitor = new Thread(() -> {
                while (true) {
                    if (cyclicBarrier.isBroken()) {
                        int numberWaiting = cyclicBarrier.getNumberWaiting();
                        System.out.println(String.format("%s 线程说：你怎么broken了啊。我帮你恢复下,唤醒数量%d", Thread.currentThread().getName(), numberWaiting));
                        cyclicBarrier.reset();
                    }
                    ConcurrentUtils.sleep(100);
                }
            }, "monitor");
            monitor.setDaemon(true);
            monitor.start();
            addTask(true);

        } finally {
            executorService.shutdownNow();
        }
    }

    private static void addTask(Boolean flag) {
        IntStream.range(1, permit + 1).forEach(i -> {
            Runnable thread = () -> {
                if (flag && i == permit) {
                    return;
                }
                System.out.println(String.format("%s 线程说我准备好了，等小伙们了。。。。", Thread.currentThread().getName()));
                try {
                    cyclicBarrier.await();
                    System.out.println(String.format("%s 线程说我终于自由了，我们一起请求了，看看你的性能怎么样", Thread.currentThread().getName()));
                } catch (InterruptedException e) {
                    System.out.println(String.format("%s 线程说：谁把我中断了", Thread.currentThread().getName()));
                } catch (BrokenBarrierException e) {
                    System.out.println(String.format("%s 线程说：谁被中断了，我竟然被broken了", Thread.currentThread().getName()));
                }

            };
            executorService.submit(thread);
        });
    }

    private static void testAPI() {
        //模拟重复请求调用；
        //while (true) {
        if (cyclicBarrier.isBroken()) {
            System.out.println(String.format("%s 线程说：你怎么broken了啊。我帮你恢复下", Thread.currentThread().getName()));
            cyclicBarrier.reset();
        }
        ConcurrentUtils.sleep(2000);
        System.out.println(String.format("%s 线程说： 小伙伴们准备好了吗？我们的工作来了", Thread.currentThread().getName()));
        addTask(false);
        //  }
    }
}
