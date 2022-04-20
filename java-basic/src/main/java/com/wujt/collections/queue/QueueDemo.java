package com.wujt.collections.queue;

import java.util.PriorityQueue;
import java.util.concurrent.DelayQueue;

/**
 * 基础API
 * //插入元素，成功返回true，失败抛出异常
 * boolean add(E e);
 * <p>
 * //插入元素，成功返回true，失败返回false或抛出异常
 * boolean offer(E e);
 * <p>
 * //取出并移除头部元素，空队列抛出异常
 * E remove();
 * <p>
 * //取出并移除头部元素，空队列返回null
 * E poll();
 * <p>
 * //取出但不移除头部元素，空队列抛出异常
 * E element();
 * <p>
 * //取出但不移除头部元素，空队列返回null
 * E peek();
 * <p>
 * 实现原理：
 * 底层基于数组和链表实现存储；
 * 提供队列的基本特性： add,offer,poll,peek
 * <p>
 * 有界队列： 队列的长度是有限的；当达到限制时入队需要专门处理
 * <p>
 * 优先队列： 通过对元素利用比较器实现对元素进行重新排序存储，从而实现优先级特性
 * <p>
 * 阻塞队列：利用Condition notEmpty、Condition notFull; 实现put,take 函数的阻塞和唤醒
 * <p>
 * 延迟队列： 利用优先级队列实现内部存储有序的元素；然后在通过计算延迟进行await后获取可以获取的数据；
 * <p>
 * 并发安全队列：ConcurrentLinkedQueue
 *
 * @author wujt
 */
public class QueueDemo {

    public static void main(String[] args) {
        //优先队列： 实现按照比较器进行排序
        // 需要实现Comparator
        PriorityQueue<String> priorityQueue = new PriorityQueue<>(10);
        priorityQueue.add("1");
        priorityQueue.offer("2");
        String peek = priorityQueue.peek();
        String poll = priorityQueue.poll();
        String element = priorityQueue.element();
        System.out.println(String.format("peek=%s,poll=%s,element=%s", peek, poll, element));

        // 延迟队列；
        // 队里不为空Condition available.awaitNanos(delay);队里为空 available.await();
        DelayQueue<Worker> delayQueue = new DelayQueue<>();
        Worker task1 = new Worker("task1", 1000);
        Worker task2 = new Worker("task2", 10000);
        delayQueue.add(task1);
        delayQueue.offer(task2);


        try {
            while (!delayQueue.isEmpty()) {
                Worker take = delayQueue.take();
                System.out.println(take);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }
}
