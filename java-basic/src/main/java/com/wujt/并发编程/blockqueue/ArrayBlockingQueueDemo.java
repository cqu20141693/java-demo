package com.wujt.并发编程.blockqueue;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * add 添加时满了会抛异常
 * offer 满了返回false
 * put满了阻塞，利用notFull.await 进行等待；
 * @author wujt
 */
public class ArrayBlockingQueueDemo {
    public static void main(String[] args) {

        ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(10);
    }
}
