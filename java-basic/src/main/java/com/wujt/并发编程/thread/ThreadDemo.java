package com.wujt.并发编程.thread;

import com.wujt.并发编程.util.ConcurrentUtils;

/**
 * Thread implements Runnable
 * <p>
 * fields:
 * private volatile String name ： 名称
 * <p>
 * private int priority ： 优先级  1-10
 * <p>
 * Whether or not the thread is a daemon thread.
 * private boolean daemon=false ： 线程是否为守护线程
 * The group of this thread
 * <p>
 * ThreadGroup group ：线程组
 * <p>
 * What will be run.
 * <p>
 * Runnable target ： 线程运行Runnable
 * <p>
 * The context ClassLoader for this thread
 * <p>
 * ClassLoader contextClassLoader : 线程类加载器
 * <p>
 * ThreadLocal values pertaining to this thread. This map is maintained
 * by the ThreadLocal class.
 * <p>
 * ThreadLocal.ThreadLocalMap threadLocals=null; ： 线程级本地变量
 * <p>
 * InheritableThreadLocal values pertaining to this thread. This map is
 * maintained by the InheritableThreadLocal class.
 * <p>
 * ThreadLocal.ThreadLocalMap inheritableThreadLocals=null; ： 继承自父线程本地变量
 * <p>
 * private volatile UncaughtExceptionHandler uncaughtExceptionHandler ： 线程异常处理器
 * <p>
 * Java thread status for tools
 * <p>
 * private volatile int threadStatus = 0; ： 线程状态
 * <p>
 * NEW ：创建后没有启动
 * RUNNABLE ： 获取到执行权
 * BLOCKED ： 在获取锁资源失败
 * WAITING ：自己调用wait,或者其他线程join
 * TIMED_WAITING : sleep,join
 * TERMINATED : 线程被终止或者执行完成
 * <p>
 * <p>
 * <p>
 * API：
 * static Thread currentThread() ： 获取当前线程对象
 * ClassLoader getContextClassLoader() ： 获取现在类加载器
 * Thread.UncaughtExceptionHandler getUncaughtExceptionHandler()
 * long getId()
 * String getName()
 * int getPriority()
 * Thread.State getState() ： 获取线程状态
 * ThreadGroup getThreadGroup()
 * static boolean holdsLock(Object obj) ： 判断线程是否具有某个对象的锁
 * void interrupt() ： 中断当前线程
 * static boolean interrupted() ： 测试当前（current thread）线程是否中断，会清除线程的中断状态。
 * public boolean isInterrupted() : 测试调用线程是否中断
 * static void yield() ： 线程主动放弃CPU执行权，进入runnable 状态；让处理器进行重新调度；不等于阻塞，可能也会下次执行
 * static void sleep(long millis) : 线程进入time_waiting ;保持有执行权
 * public final void join() throws InterruptedException : 将当前线程阻塞；直到join线程执行完成被唤醒；
 * 可以放到一个线程被开启之前，实现happens-before 功效；当线程被中断会抛出异常；
 * public synchronized void start() ：启动线程的方法
 * <p>
 * <p>
 * 杀死线程的方式
 *
 * @author wujt
 */
public class ThreadDemo {

    public static void main(String[] args) {
        Thread.yield();
        /**
         *  yield : 当前线程放弃执行机会；进行公平竞争
         */
        testYield();
        /**
         * 线程需要获取执行机会；将当前线程阻塞；知道线程执行完成或者被中断
         * 可以实现两个线程执行的先后实现；t1.join(),t2.start()
         */
        testJoin();
        /**
         * 对象锁
         * 在使用wait() ,notify(),notifyAll() 方法前必须获取对象锁 Synchronized
         * wait 会让线程进入waiting 状态，只能等待notify 唤醒进入runnable;
         * 而sleep 只是让线程休眠一定时间后立即进入runnable 队里中，没有放弃可执行权
         */
        testWait();

        /**
         * 测试interrupt(): 该方法只是设置线程的中断位，并不会真实中断线程；而需要线程自己去捕获InterruptException;
         * 使用场景：
         * 当sleep方法被调用。
         * 当wait,join方法被调用。
         * 当被I/O阻塞，可能是文件或者网络等等。
         *
         *interrupted() :静态方法，判断线程中断状态，并且会清除线程的中断状态。
         *
         *isInterrupted(): 断线程中断状态，但不会清除线程中断状态。
         *
         * 线程停止的几种情况：
         * 1： 使用退出标记，run方法执行完毕，线程正常退出。
         * 2： 使用stop()方法，已过时的方法，不推荐。
         * 3： 使用interrupt()方法中断线程。
         */
        testInterrupte();

        /**
         *  Thread 放弃的API
         *  stop 极度不安全；当我们能确定线程不会有任何的影响的时候；可以直接调用；
         *  比如当新开线程执行脚本，并不能控制脚本的逻辑时；
         */
        testDeprecated();
        // 当前线程放弃执行权限。
        Thread.yield();
    }

    private static void testDeprecated() {
        InterruptedThead interruptedThead = new InterruptedThead(() -> {
            System.out.println("test interrupted");
        }, "testInterrupted");
        interruptedThead.start();
        interruptedThead.stop();
    }

    private static void testInterrupte() {

        Thread.currentThread().interrupt();
        boolean interrupted = Thread.currentThread().isInterrupted();
        if (interrupted) {
            System.out.format("Thread=%s is isInterrupted\n", Thread.currentThread().getName());
        }
        if (Thread.interrupted()) {
            System.out.format("Thread=%s is interrupted\n", Thread.currentThread().getName());
        }
        boolean interrupted2 = Thread.currentThread().isInterrupted();
        if (interrupted2) {
            System.out.format("Thread=%s is interrupted\n", Thread.currentThread().getName());
        }
        InterruptedThead interruptedThead = new InterruptedThead(() -> {
            System.out.println("test interrupted");
        }, "testInterrupted");
        interruptedThead.start();
        interruptedThead.interrupt();
        MyThead myThead = new MyThead(() -> {
            System.out.println("test interruptException");
        }, "testInterruptException");
        myThead.start();
        myThead.interrupt();
    }

    private static void  testWait() {
        MyThead myThead = new MyThead(() -> {
            System.out.println("run target");
        }, "testWait");
        testNotify(myThead);
    }

    private static void testNotify(MyThead myThead) {
        myThead.start();
        while (!myThead.getState().equals(Thread.State.TERMINATED)) {
            if (myThead.getState().equals(Thread.State.WAITING)) {
                Object sigLock = myThead.getSigLock();
                synchronized (myThead.getSigLock()) {
                    sigLock.notify();
                }
            }
            ConcurrentUtils.sleep(10);
        }
    }

    private static void testJoin() {
        Thread t = new Thread(() -> {
            System.out.println("First task started");
            System.out.println("Sleeping for 2 seconds");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("First task completed");
        });
        Thread t1 = new Thread(() -> System.out.println("Second task completed"), "join");
        t.start(); // Line 15
        try {
            t.join(); // Line 16
        } catch (InterruptedException e) { // join 线程被中断会抛出异常
            System.out.println(String.format("name=%s Thread is Interrupted", t.getName()));
        }
        t1.start();
    }

    /**
     * 生产者和消费都会放弃CPU执行机会；由操作系统重新进行调度；
     * 优先级高的被调度的可能性高
     */
    private static void testYield() {
        Thread producer = new Producer();
        Thread consumer = new Consumer();

        producer.setPriority(Thread.MAX_PRIORITY); //Min Priority
        consumer.setPriority(Thread.MIN_PRIORITY); //Max Priority

        producer.start();
        consumer.start();
    }

}
