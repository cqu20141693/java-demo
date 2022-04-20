package com.gow;

import com.gow.diruptor.DisruptorQueue;
import com.gow.diruptor.DisruptorQueueFactory;
import com.gow.diruptor.MessageEventFactory;
import com.gow.domain.DataInfo;
import com.gow.eventhandler.ClearingEventHandler;
import com.gow.workhandler.LogWorkHandler;
import com.lmax.disruptor.YieldingWaitStrategy;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

/**
 * @author gow 2021/06/13
 */
public class DisruptorTest {

    @Test
    public void testWorkDisruptor() {

        LogWorkHandler logWorkHandler = new LogWorkHandler();

        DisruptorQueue workPoolQueue = DisruptorQueueFactory.getWorkPoolQueue(new MessageEventFactory(), 1024 * 1024, false, new YieldingWaitStrategy(), logWorkHandler, logWorkHandler);
        IntStream.iterate(0, sqe -> sqe < 100, cur -> cur + 2).forEach(index -> {
            DataInfo dataInfo = new DataInfo();
            dataInfo.setType(1);
            dataInfo.setValue(index);
            workPoolQueue.add(dataInfo);
        });

    }

    @Test
    public void testEventDisruptor() {

        ClearingEventHandler clearingEventHandler = new ClearingEventHandler();

        DisruptorQueue workPoolQueue = DisruptorQueueFactory.getHandleEventsQueue(new MessageEventFactory(), 1024 * 1024, false, new YieldingWaitStrategy(), clearingEventHandler, clearingEventHandler);
        IntStream.iterate(0, sqe -> sqe < 100, cur -> cur + 2).forEach(index -> {
            DataInfo dataInfo = new DataInfo();
            dataInfo.setType(1);
            dataInfo.setValue(index);
            workPoolQueue.add(dataInfo);
        });

    }
}
