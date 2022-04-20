package com.wujt.并发编程.thread;

/**
 * @author wujt
 */
public class Producer extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println("I am Producer : Produced Item " + i);
            Thread.yield();
        }
    }
}
