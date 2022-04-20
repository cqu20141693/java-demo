package com.gow.eventhandler;

import com.gow.diruptor.DisruptorConsumer;
import com.gow.domain.DataInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gow 2021/06/13
 */
@Slf4j
public class ClearingEventHandler extends DisruptorConsumer<DataInfo> {
    @Override
    public void consume(DataInfo message) {
        log.info("do clear dataInfo={}", message);
        message.clear();
    }
}
