package com.wujt.quartz.domain;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.impl.JobExecutionContextImpl;

/**
 * 定义自己的执行任务
 * @author wujt
 */
@Slf4j
public class PowerYourselfJob extends BaseJob {
    @Override
    public void execute(JobExecutionContext context) {
        JobExecutionContextImpl jobExecutionContext= (JobExecutionContextImpl) context;
        jobExecutionContext.getTrigger().getJobDataMap();
        System.out.println(context.getJobDetail().getJobDataMap());
        System.out.println("11111111111111");
    }
}