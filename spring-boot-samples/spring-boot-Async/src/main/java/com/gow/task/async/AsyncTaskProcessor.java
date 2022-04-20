package com.gow.task.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/4 0004
 */
@Component
@Slf4j
public class AsyncTaskProcessor {

    @Async
    public void handleData() {
        log.info("handleData");
    }
    @Async("otherExecutor")
    public void handleOther() {
        log.info("handleOther");
    }
}
