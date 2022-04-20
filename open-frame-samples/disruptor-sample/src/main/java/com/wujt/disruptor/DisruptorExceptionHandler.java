package com.wujt.disruptor;

import com.lmax.disruptor.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wujt  2021/5/8
 */
@Slf4j
public class DisruptorExceptionHandler<T> implements ExceptionHandler<MessageEvent<T>>{
    @Override
    public void handleEventException(Throwable ex, long sequence, MessageEvent<T> event) {
        ex.printStackTrace();
        log.error("handleEventException sequence={} event={}", sequence, event);
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        ex.printStackTrace();
        log.error("handleOnStartException");
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        ex.printStackTrace();
        log.error("handleOnShutdownException");
    }
}
