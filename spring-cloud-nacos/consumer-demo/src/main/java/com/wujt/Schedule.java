package com.wujt;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/12/14
 */
//@Component
@EnableScheduling
public class Schedule {



    @Scheduled(fixedDelay = 1000)
    public void printMetrix(){

    }
}
