package com.wujt.disruptor.sample;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.wujt.disruptor.DisruptorQueue;
import com.wujt.disruptor.DisruptorQueueFactory;
import com.wujt.disruptor.MessageEvent;
import com.wujt.disruptor.MessageEventFactory;

/**
 * @author wujt  2021/5/8
 */
public class OneToOneDisruptorTest {
    public static void main(String[] args) throws InterruptedException {
        LogConsumer logConsumer = new LogConsumer();
        EventFactory<MessageEvent<Log>> tMessageEventFactory = new MessageEventFactory<>();
        DisruptorQueue<Log> disruptorQueue = DisruptorQueueFactory.getHandleEventsQueue(tMessageEventFactory, 4, false, new SleepingWaitStrategy(), logConsumer);
        DisruptorProducer disruptorProducer = new DisruptorProducer("producer1", disruptorQueue);
        Thread thread = new Thread(disruptorProducer);
        thread.start();
        // 执行3s后，生产者不再生产
        Thread.sleep(3 * 1000);

        disruptorProducer.stopThread();
        // 确认在
        disruptorQueue.shutdown();
    }
}
