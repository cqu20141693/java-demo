package com.wujt.并发编程.juc_lock;

/**
 * @author wujt
 */
public class MySimpleLockDemo {
    private int i = 0;

    MySimpleLock myLock = new MySimpleLock();

    private void add() {
        myLock.lock();
        try {
            i++;
        } finally {
            myLock.unlock();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        MySimpleLockDemo mySimpleLockDemo = new MySimpleLockDemo();
        for (int j = 0; j < 4; j++) {
            new Thread(() -> {
                for (int k = 0; k < 1000; k++) {
                    mySimpleLockDemo.add();
                }
            }).start();
        }
        Thread.sleep(2000);
        System.out.println("结果：" + mySimpleLockDemo.i);

    }
}
