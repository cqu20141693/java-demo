package com.gow.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author wujt  2021/5/25
 */
@Slf4j
public class MyListener implements ApplicationListener<SpringApplicationEvent> {

    @Override
    public void onApplicationEvent(SpringApplicationEvent applicationEvent) {
        log.info("applicationEvent={} trigger",applicationEvent.getClass());
    }
}
