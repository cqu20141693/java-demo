package com.gow.workhandler;

import com.gow.diruptor.DisruptorConsumer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gow 2021/06/13
 */
@Slf4j
public class LogWorkHandler extends DisruptorConsumer {
    @Override
    public void consume(Object message) {
        log.info("receiver message={}", message);
    }
}
