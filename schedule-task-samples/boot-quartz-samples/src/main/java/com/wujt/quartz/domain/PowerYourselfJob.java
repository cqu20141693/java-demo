package com.wujt.quartz.domain;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

/**
 * 定义自己的执行任务
 * @author wujt
 */
@Slf4j
//@Component
public class PowerYourselfJob extends BaseJob {

    @Override
    public void execute(JobExecutionContext context) {

        System.out.println(context.getJobDetail().getJobDataMap());
        System.out.println("11111111111111");
    }
}