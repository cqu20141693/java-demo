package com.gow.diruptor;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author gow 2021/05/09
 */
@Slf4j
public class DisruptorProducer<T> {
    private DisruptorQueue<T> disruptorQueue;
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Boolean flag = true;

    public DisruptorProducer(DisruptorQueue<T> disruptorQueue) {
        this.disruptorQueue = disruptorQueue;
    }

    public Boolean sendWithTranslator(T message) {
        try {
            readWriteLock.readLock().lock();
            if (!flag) {
                log.info("DisruptorProducer is closed");
                return false;
            }
            disruptorQueue.addWithTranslator(message);
        } finally {
            readWriteLock.readLock().unlock();
        }
        return true;
    }

    /**
     * 发送成功
     *
     * @param message
     * @return true 表示成功，false 失败
     */
    public Boolean send(T message) {
        try {
            readWriteLock.readLock().lock();
            if (!flag) {
                log.info("DisruptorProducer is closed");
                return false;
            }
            disruptorQueue.add(message);
        } finally {
            readWriteLock.readLock().unlock();
        }
        return true;
    }


    /**
     * 暂停producer
     */
    public void stop() {
        log.info("DisruptorProducer stop invoked");
        readWriteLock.writeLock().lock();
        try {
            this.flag = false;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

}
