package com.gow.task.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/4 0004
 */
@Component
@Slf4j
public class ScheduleTask {
    @Scheduled(initialDelay = 1000, fixedRate = 5000)
    public void doSomething() {
        // something that should run periodically
        log.info("schedule fixedRate");
    }
    @Scheduled(cron="*/5 * * * * 1-7")
    public void cornDoSomething() {
        // something that should run on weekdays only
        log.info("schedule cornDoSomething");
    }
    @Scheduled(fixedDelay=5000)
    public void fixedDelayDoSomething() {
        // something that should run periodically
        log.info("schedule fixedDelayDoSomething");
    }
}
