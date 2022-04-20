package com.wujt.collections.queue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/**
 * 底层通过数组存储数据；利用堆排序进行数据的排序存储；
 *
 * 队列获取操作 poll、remove、peek 和 element 访问处于队列头的元素
 * @author wujt
 */
public class PriorityQueueDemo {
    public static void main(String[] args) {
        List<Integer> list=new ArrayList<Integer>(){
            {
                add(1);
                add(5);
                add(2);
                add(23);
                add(32);
                add(6);
            }
        };
        // 大顶堆就是让后者大于前者，每次扩容是扩容1，会出现频繁的扩容
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(6,(o1,o2)-> o2-o1);
        priorityQueue.addAll(list);
        priorityQueue.add(100);
        // 如果要实现k顶堆，每次操作时做检查，每一次添加前先进行一次poll操作，再进行一次offer
        priorityQueue.offer(10);
        System.out.println(Arrays.toString(priorityQueue.toArray()));
        while (priorityQueue.size()>0){
            System.out.print( priorityQueue.poll()+" ");
        }

    }
}
