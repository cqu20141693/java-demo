package com.wujt.并发编程.thread;

/**
 * @author wujt
 */
public class InterruptedThead extends Thread {


    public InterruptedThead(Runnable target, String name) {
        super(target, name);
    }

    @Override
    public void run() {
        super.run();
        for (; ; ) {
            if (Thread.interrupted()) {
                System.out.format("Thread=%s is interrupted\n", Thread.currentThread().getName());
                break;
            }
        }
    }
}
