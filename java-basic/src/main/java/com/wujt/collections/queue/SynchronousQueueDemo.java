package com.wujt.collections.queue;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

/**
 * SynchronousQueue 是一个不存储数据的队列
 * 当生产者进行生产 put() 阻塞offer（） 非阻塞方式调用时。如果没有消费者消费；生产者要么阻塞；要么失败offer;
 *
 * 在线程池newCachedThreadPool中进行了使用 offer 和 take 结合使用
 *
 *
 * 生产者向队列中放入数据；如果队列存在数据；直接阻塞当前生产者；如果没得数据；加入数据；并将消费线程进行唤醒。
 * 消费者消费数据时；如果当前队列没有数据；直接阻塞；如果存在数据，直接消费（消费过程肯定是有锁）。
 *
 * @author wujt
 */
public class SynchronousQueueDemo {
    static class SynchronousQueueProducer implements Runnable {

        protected BlockingQueue<String> blockingQueue;
        final Random random = new Random();

        public SynchronousQueueProducer(BlockingQueue<String> queue) {
            this.blockingQueue = queue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String data = UUID.randomUUID().toString();
                    System.out.println("Put: " + data);
                    blockingQueue.put(data);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    static class SynchronousQueueConsumer implements Runnable {

        protected BlockingQueue<String> blockingQueue;

        public SynchronousQueueConsumer(BlockingQueue<String> queue) {
            this.blockingQueue = queue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String data = blockingQueue.take();
                    System.out.println(Thread.currentThread().getName()
                            + " take(): " + data);
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) {
        final SynchronousQueue<String> synchronousQueue = new SynchronousQueue<>();

        SynchronousQueueProducer queueProducer = new SynchronousQueueProducer(synchronousQueue);
        new Thread(queueProducer).start();
        SynchronousQueueConsumer queueConsumer = new SynchronousQueueConsumer(synchronousQueue);
        new Thread(queueConsumer).start();
       SynchronousQueueConsumer queueConsumer1= new SynchronousQueueConsumer(synchronousQueue);
       new Thread(queueConsumer1).start();

        /**
         *其中使用了SynchronousQueue 作为阻塞队列
         */
        ExecutorService service= Executors.newFixedThreadPool(10);

    }
}
