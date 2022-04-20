package com.wujt.component;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import com.wujt.model.Element;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wujt
 */
@Slf4j
public class LowEventHandler implements EventHandler<Element>, WorkHandler<Element> {
    @Override
    public void onEvent(Element element) {
        log.info("LowHandler-->event: {}", element);
    }

    @Override
    public void onEvent(Element element, long sequence, boolean endOfBatch) {
        log.info("LowHandler.onEvent()-->event: {}, sequence: {}, endOfBatch: {}", element, sequence, endOfBatch);
    }
}
