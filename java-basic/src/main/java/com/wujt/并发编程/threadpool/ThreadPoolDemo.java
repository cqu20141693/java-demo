package com.wujt.并发编程.threadpool;

import com.gow.concurrent.BizThreadFactory;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * fields:
 * <p>
 * 线程池的控制状态,用高3位来表示线程池的运行状态,低29位来表示线程池中工作线程的数量
 * private final AtomicInteger ctl=new AtomicInteger(ctlOf(RUNNING,0));
 * 线程池的运行状态，总共有5个状态，用高3位来表示
 * private static final int RUNNING    = -1 << COUNT_BITS;  高3位为111，接受新任务并处理阻塞队列中的任务
 * private static final int SHUTDOWN   =  0 << COUNT_BITS;  高3位为000，不接受新任务但会处理阻塞队列中的任务
 * private static final int STOP       =  1 << COUNT_BITS;  高3位为001，不会接受新任务，也不会处理阻塞队列中的任务，并且中断正在运行的任务
 * private static final int TIDYING    =  2 << COUNT_BITS;
 * 高3位为010，所有任务都已终止，工作线程数量为0，线程池将转化到TIDYING状态，即将要执行terminated()钩子方法
 * private static final int TERMINATED =  3 << COUNT_BITS;  高3位为011，terminated()方法已经执行结束
 * 线程池状态的所有转换情况
 * RUNNING -> SHUTDOWN：调用shutdown()，可能在finalize()中隐式调用
 * (RUNNING or SHUTDOWN) -> STOP：调用shutdownNow()
 * SHUTDOWN -> TIDYING：当缓存队列和线程池都为空时
 * STOP -> TIDYING：当线程池为空时
 * TIDYING -> TERMINATED：当terminated()方法执行结束时
 * 通常情况下，线程池有如下两种状态转换流程
 * RUNNING -> SHUTDOWN -> TIDYING -> TERMINATED
 * RUNNING -> STOP -> TIDYING -> TERMINATED
 *
 * <p>
 * 任务缓存队列，用来存放等待执行的任务
 * private final BlockingQueue<Runnable> workQueue;
 * 全局锁，对线程池状态等属性修改时需要使用这个锁
 * private final ReentrantLock mainLock = new ReentrantLock();
 * 线程池中工作线程的集合，访问和修改需要持有全局锁
 * private final HashSet<Worker> workers = new HashSet<Worker>();
 * 终止条件
 * private final Condition termination = mainLock.newCondition();
 * 线程池中曾经出现过的最大线程数
 * private int largestPoolSize;
 * //已完成任务的数量
 * private long completedTaskCount;
 * <p>
 * <p>
 * //线程工厂
 * private volatile ThreadFactory threadFactory;
 * //任务拒绝策略
 * private volatile RejectedExecutionHandler handler;
 * 线程池提供了4种任务拒绝策略：
 * AbortPolicy：丢弃任务并抛出RejectedExecutionException异常，默认策略；
 * CallerRunsPolicy：由调用execute方法的线程执行该任务；
 * DiscardPolicy：丢弃任务，但是不抛出异常;
 * DiscardOldestPolicy：丢弃阻塞队列最前面的任务，然后重新尝试执行任务（重复此过程）。
 * 当然也可以根据应用场景实现RejectedExecutionHandler接口，自定义饱和策略，如记录日志或持久化存储不能处理的任务。
 * <p>
 * //线程存活时间
 * private volatile long keepAliveTime;
 * //核心池大小，若allowCoreThreadTimeOut被设置，核心线程全部空闲超时被回收的情况下会为0
 * private volatile int corePoolSize;
 * //最大池大小，不得超过CAPACITY
 * private volatile int maximumPoolSize;
 * <p>
 * <p>
 * API:
 * public ThreadPoolExecutor(int corePoolSize,int maximumPoolSize,long keepAliveTime,TimeUnit
 * unit,BlockingQueue<Runnable> workQueue,ThreadFactory threadFactory,RejectedExecutionHandler handler)
 * <p>
 * public Future<?> submit(Runnable task);
 * public <T> Future<T> submit(Runnable task, T result);
 * public <T> Future<T> submit(Callable<T> task);
 * <p>
 * 任务提交执行流程：
 * 1.是提交任务submit(),
 * 2.对任务包装为可执行task:newTaskFor(task, null);
 * 3.执行任务task; execute(task)
 * 4. 获取线程状态，判断当前线程数是否大于核心线程数；如果不大于；启动一个核心线程执行当前任务： addWorker(command, true)
 * 5. 核心线程都已经启动；判断线程池是否为运行状态，为运行状态将任务放到workQueue： workQueue.offer(command)；其中会进行二次判断线程是否为运行状态；如果不是走拒绝策略
 * 6. 如果线程不为运行状态：走拒绝策略：  reject(command)
 * <p>
 * 创建线程策略：addWorker(Runnable firstTask, boolean core)
 * 1.原子性的增加workerCount： 只有当完成线程数更新成功；表示可以进行线程创建；其中包括对并发处理和线程关闭处理
 * 2.将用户给定的任务封装成为一个worker，并将此worker添加进workers集合中 ： 创建真正执行的线程，将任务放到线程中，
 * 3.启动worker对应的线程 ： 启动执行线程
 * 4.若线程启动失败，回滚worker的创建动作，即从workers中移除新添加的worker，并原子性的减少workerCount
 * 5. 整个过程中都会检查线程池是否为运行状态
 * <p>
 * 工作线程原理：final class Worker extends AbstractQueuedSynchronizer implements Runnable
 * fields:
 * 用来封装worker的线程，线程池中真正运行的线程,通过线程工厂创建而来
 * final Thread thread;
 * //worker所对应的第一个任务，可能为空
 * Runnable firstTask;
 * //记录当前线程完成的任务数
 * volatile long completedTasks;
 * <p>
 * 自实现了同步器；主要功能是在执行任务的过程中保护线程；保证线程在执行任务的过程中不会被中断，
 * <p>
 * 工作线程执行过程：
 * 线程循环获取任务队里中的任务：getTask() -> Runnable r = timed ?workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS)
 * :workQueue.take();
 * 如果队里为空，则会阻塞线程；如果获取到任务；执行任务； 任务执行完成后尽心各种统计。
 * <p>
 * 1.线程池处于RUNNING状态，阻塞队列不为空，返回成功获取的task对象
 * 2.线程池处于SHUTDOWN状态，阻塞队列不为空，返回成功获取的task对象
 * 3.线程池状态大于等于STOP，返回null，回收线程
 * 4.线程池处于SHUTDOWN状态，并且阻塞队列为空，返回null，回收线程
 * 5.worker数量大于maximumPoolSize，返回null，回收线程
 * 6.线程空闲时间超时，返回null，回收线程
 * <p>
 * 在获取任务时；如果当前线程数量大于核心线程时；如果获取到的任务为空null, 则会循环借宿，将线程回收；
 * 如果线程的数量不大于核心线程；则会阻塞式获取任务： workQueue.take();
 * <p>
 * <p>
 * 1. 线程池的核心参数： 核心线程，最大线程；线程的活跃时长，线程工厂，拒绝策略
 * 2. 任务提交过程？
 * 3. 线程的创建过程？addWorker()方法完成了如下几件任务：
 * 原子性的增加workerCount
 * 将用户给定的任务封装成为一个worker，并将此worker添加进workers集合中
 * 启动worker对应的线程
 * 若线程启动失败，回滚worker的创建动作，即从workers中移除新添加的worker，并原子性的减少workerCount
 *
 * 4. 线程的执行过程，任务的获取，线程的回收点？
 * 5. 线程池核心fields: ctl，workeQueue,workers ？
 * 6. 线程池框架提供了两种方式提交任务，submit()和execute()，通过submit()方法提交的任务可以返回任务执行的结果，通过execute()方法提交的任务不能获取任务执行的结果。
 * https://mp.weixin.qq.com/s?__biz=MjM5NjQ5MTI5OA==&mid=2651751537&idx=1&sn=c50a434302cc06797828782970da190e&chksm
 * =bd125d3c8a65d42aaf58999c89b6a4749f092441335f3c96067d2d361b9af69ad4ff1b73504c&mpshare=1&scene=1&srcid
 * =&sharer_sharetime=1585844357847&sharer_shareid=5a6333f48cffa6a8b913f91e8a917729#rd
 *
 * @author wujt
 */

public class ThreadPoolDemo {
    public static void main(String[] args) {

        /**
         * new ThreadPoolExecutor(1, 1,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>()))
         * 表示线程池中一直只会存在一个消费者线程； 任务队列是链式阻塞队列
         */
        ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor();
        threadPoolExecutor.submit(() -> System.out.println("hello"));
        threadPoolExecutor.shutdown();


        /**
         * new ThreadPoolExecutor(0, Integer.MAX_VALUE,60L, TimeUnit.SECONDS,new SynchronousQueue<Runnable>()
         *
         * 表示线程池核心线程为0； 通过同步阻塞队列实现当生产一个任务时；需要存在一个消费者线程消费；开始不存在就重新创建执行任务；
         * 一直存在任务起始会一直创建消费者线程（max=Intege.Max）; 当存在一个消费者线程执行完任务有；在60秒内会去消费队列中的任务；
         * 这样就达到了消费者线程的重用；类似于自适应的线程；但是如果生产者很快；消费很慢；会创建很多线程；出现OOM
         *
         * 应用场景不是很清楚 ： 感觉是固定生产者速率的进行自适应 测试： 然后再确定用其他的线程池模式
         */
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        /**
         * new ThreadPoolExecutor(nThreads, nThreads,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>())
         * 线程池的核心线程和最大线程数为指定线程数；当生产者生产任务后；直接使用线程池中的核心线程进行处理；
         *
         * 使用情况一般为任务比较充足；通过测试找出合适的核心线程数据量
         */
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        /**
         *  ThreadPoolExecutor(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS,new DelayedWorkQueue())
         *
         *   利用延迟队列实现定时执行任务；
         *   在任务增加中利用装饰器ScheduledFutureTask 对command 进行装饰；实现延迟队列的过期时间计算和延迟队列比较方法
         *
         *   之后将任务放入队列中 ：delayedExecute()
         *   再之后检查执行线程,增加核心线程处理任务：ensurePrestart()；
         *
         *   当线程在处理任务后，会进行take
         */
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
        // 任务执行完成后以固定周期执行
        scheduledExecutorService.scheduleAtFixedRate(() -> System.out.println("schedule"), 0, 10, TimeUnit.MINUTES);
        // 任务以固定的周期执执行；不论之前的任务任务是否完成
        scheduledExecutorService
                .scheduleWithFixedDelay(() -> System.out.println("schedule with fixed delay"), 0, 10, TimeUnit.MINUTES);
        scheduledExecutorService.schedule(() -> System.out.println("schedule"), 10, TimeUnit.MINUTES);

        ThreadFactory threadFactory = BizThreadFactory.create("task-", false);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1, //  核心线程数，特别的为零时分析
                5, // 最大线程数，在队列满后提交任务创建
                30, // 线程空闲的最大时间
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(5120), // 阻塞队列
                threadFactory, // 线程工厂，可以配置线程优先级，线程名称，线程的daemon 属性
                new ThreadPoolExecutor.AbortPolicy()); // 线程池资源不够时，任务拒绝策略
        // api
        executor.prestartCoreThread(); // 创建线程池时创建线程，默认不会，需要提交任务时创建
        executor.allowCoreThreadTimeOut(true);  // 允许核心线程
        Future<?> task = executor.submit(() -> System.out.println("submit task")); // submit,future 可以阻塞式获取任务执行结果
        executor.execute(() -> System.out.println("execute task")); // execute，提交任务，线程池线程消费任务执行，不关心任务执行结果
    }
}
