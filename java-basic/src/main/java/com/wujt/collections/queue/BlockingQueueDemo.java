package com.wujt.collections.queue;

import java.util.concurrent.*;

/**
 * 阻塞队列
 * <p>
 * <p>
 * 附加操作：
 * offer(E) ： 当队列满，生产线程阻塞
 * <p>
 * 新增API
 * take() : 当队列空，消费线程阻塞
 * void put(E e) : 如队列满则一直阻塞直到有空位可以插入元素
 * <p>
 * 阻塞实现原理： 参考并发编程中 blockqueue  模块
 * 基本都是通过Lock实现竟态条件的安全性： 多个线程进行并发添加和删除操作时，利用可重入锁进行并发控制
 * 和Condition实现生产消费的协调： 当队里为空想要读数据时和队里满了加数据是 阻塞利用的condition进行阻塞和唤醒
 *
 * @author wujt
 */
public class BlockingQueueDemo {
    public static void main(String[] args) {
        // 同步阻塞队列 ： 不存储元素的阻塞队列
        BlockingQueue<String> blockingQueue = new SynchronousQueue<>();
        // 链式阻塞队列：有界阻塞队列 --> LinkedBlockingQueue(Integer.MAX_VALUE);
        LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>();
        // 链式阻塞队列：无界阻塞队列
        LinkedTransferQueue<Object> linkedTransferQueue = new LinkedTransferQueue<>();
        // 链式双向阻塞队列：有界阻塞队列
        LinkedBlockingDeque<Object> linkedBlockingDeque = new LinkedBlockingDeque<>();
        // 数组阻塞队列：有界队列
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(10);
        //优先级阻塞队列
        PriorityBlockingQueue<Object> priorityBlockingQueue = new PriorityBlockingQueue<>();
        // 延迟阻塞队列
        // 内部通过PriorityQueue存储数据；
        DelayQueue<Delayed> delayQueue = new DelayQueue<>();

        // 设计一个线程池，首先是选择容器：阻塞队列（当队列中不存在空闲连接时阻塞直到回收连接）
        // 2. 需要两个容器，一个是正在使用的连接，一个是空闲连接
        // 3. 当初始化时指定一个核心连接数和最大连接数，初始化创建核心连接数个连接放到空闲连接中；
        // 4. 当获取连接进行数据库炒作时，首先是从空闲连接中获取（poll），如果获取不到，判断当前正在使用的连接是否等于最大连接数
        // 5. 等于则阻塞(take)，否在再次检查下是否有空闲，没得就创建一个连接加入正在使用的队列中并返回，
        // 6. 当连接使用完后，进行回收，调用连接的close  方法，从链表中获取连接放到空闲连接中，
    }
}
