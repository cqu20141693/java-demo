package com.wujt.disruptor.sample;

import com.wujt.disruptor.DisruptorConsumer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wujt  2021/5/8
 */
@Slf4j
public class LogConsumer extends DisruptorConsumer<Log> {

    public LogConsumer() {

    }

    @Override
    public void consume(Log message) {
        log.info("LogConsumer 消费了队里中的数据：{}", message);
    }
}
