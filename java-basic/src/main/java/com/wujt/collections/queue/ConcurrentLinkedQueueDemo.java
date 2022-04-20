package com.wujt.collections.queue;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 并发队列
 *
 * @author wujt
 */
public class ConcurrentLinkedQueueDemo {
    public static void main(String[] args) {
        //链表实现的线程安全的无界队列 : 不允许插入 null
        ConcurrentLinkedQueue<String> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        concurrentLinkedQueue.add("2");
        concurrentLinkedQueue.add("1");
        String peek = concurrentLinkedQueue.peek();
        String poll = concurrentLinkedQueue.poll();
        System.out.println(String.format("peek=%s,poll=%s", peek, poll));
    }
}
