package com.gow.event;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author wujt  2021/5/25
 */
@Component
@Slf4j
public class ReadinessStateExporter {
    @EventListener
    public void onStateChange(AvailabilityChangeEvent<ReadinessState> event) {
        log.info("AvailabilityChangeEvent={} trigger", event);
        switch (event.getState()) {
            case ACCEPTING_TRAFFIC:
                // create file /tmp/healthy
                break;
            case REFUSING_TRAFFIC:
                // remove file /tmp/healthy
                break;
            default:

        }
    }
}
