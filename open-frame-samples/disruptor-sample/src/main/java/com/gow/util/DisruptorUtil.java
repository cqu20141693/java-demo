package com.gow.util;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.wujt.model.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wujt
 */
@Component
public class DisruptorUtil {

    private final Disruptor<Element> disruptor;

    @Autowired
    public DisruptorUtil(Disruptor<Element> disruptor) {
        this.disruptor = disruptor;
    }

    public void onElement(Object element) {
        RingBuffer<Element> ringBuffer = disruptor.getRingBuffer();
        long sequence = ringBuffer.next();

        try {
            Element e = ringBuffer.get(sequence);
            BeanUtils.copyProperties(element, e);
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
