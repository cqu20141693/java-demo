package com.wujt.并发编程.thread;

/**
 * @author wujt
 */
public class Consumer extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println("I am Consumer : Consumer Item " + i);
            Thread.yield();
        }
    }
}
