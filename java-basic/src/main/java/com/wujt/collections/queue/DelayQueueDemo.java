package com.wujt.collections.queue;

import java.util.concurrent.DelayQueue;

/**
 * 底层通过PriorityQueue 优先队列存储数据；
 * 每次进行take 消费数据时；首先获取资操作优先队列的资格锁（并发访问）；然后获取优先队列的队首；如果不存在数据；直接阻塞（Blocking 队列特性）；
 * 如果存在数据；判断数据的可用时间是否满足；满足直接消费数据；不满足；阻塞 线程 可用时间-当前时间
 *
 * 多了一个取出数据后的时间判断，如果时间不满足，则阻塞当前线程 可用时间-当前时间
 * @author wujt
 */
public class DelayQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        DelayQueue<Worker> delayQueue = new DelayQueue<>();
        Worker take = delayQueue.take();
    }

}
