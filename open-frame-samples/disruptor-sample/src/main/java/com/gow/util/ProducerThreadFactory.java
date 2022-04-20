package com.gow.util;

import java.util.concurrent.ThreadFactory;

/**
 * @author wujt
 */
public class ProducerThreadFactory implements ThreadFactory {
    private int counter;
    private String name;

    public ProducerThreadFactory(String name) {
        counter = 1;
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {

        Thread t = new Thread(r, name + "-Thread_" + counter);
        counter++;
        return t;
    }
}
