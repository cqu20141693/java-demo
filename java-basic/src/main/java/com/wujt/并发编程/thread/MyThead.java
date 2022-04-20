package com.wujt.并发编程.thread;

/**
 * @author wujt
 */
public class MyThead extends Thread {

    private final Object sigLock = new Object();

    public Object getSigLock() {
        synchronized (sigLock) {
            return sigLock;
        }
    }

    public MyThead() {
    }

    public MyThead(Runnable target, String name) {
        super(target, name);
    }

    @Override
    public void run() {
        super.run();
        synchronized (sigLock) {
            try {
                sigLock.wait();
            } catch (InterruptedException e) {
                System.out.format("Thread=%s occur InterruptedException=%s\n", Thread.currentThread().getName(), e.getCause());
            }
            System.out.println("target has runned! MyThread will exit");
        }
    }
}
