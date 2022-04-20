package com.wujt;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.wujt.model.Element;

import java.util.concurrent.ThreadFactory;

/**
 * @author wujt
 */
public class DisruptorDemo {
    public static void main(String[] args) throws InterruptedException {
        // 生产者的线程工厂
        ThreadFactory producerFactory = r -> new Thread(r, "simpleThread");
        // RingBuffer生产工厂,初始化RingBuffer的时候使用
        EventFactory<Element> factory = Element::new;
        // 处理Event的handler
        EventHandler<Element> handler = (element, sequence, endOfBatch) -> System.out.println("Element: " + element.getValue());

        // 阻塞策略
        BlockingWaitStrategy strategy = new BlockingWaitStrategy();

        // 指定RingBuffer的大小
        int bufferSize = 16;

        // 创建disruptor，采用单生产者模式
        Disruptor<Element> disruptor = new Disruptor<>(factory, bufferSize, producerFactory, ProducerType.SINGLE, strategy);

        // 设置EventHandler
        disruptor.handleEventsWith(handler);

        // 启动disruptor的线程
        disruptor.start();

        RingBuffer<Element> ringBuffer = disruptor.getRingBuffer();

        int l = 0;
        while (true) {
            // 获取下一个可用位置的下标
            long sequence = ringBuffer.next();
            try {
                // 用工厂创建一个元素对象放到下标并返回
                Element event = ringBuffer.get(sequence);
                // 对数据进行操作
                event.setValue(l);
            } finally {
                // 将可用位置的元素推送出去，路由到处理器处理
                ringBuffer.publish(sequence);
                // 后续等待处理器处理数据
            }
            Thread.sleep(10);
            l++;
        }
    }
}
