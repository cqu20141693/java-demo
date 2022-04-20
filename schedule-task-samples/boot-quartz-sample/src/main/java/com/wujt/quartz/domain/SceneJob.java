package com.wujt.quartz.domain;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.JobExecutionContextImpl;

/**
 * quartz 自支持job: 将执行代码和job 捆绑
 *
 * @author wujt
 */
@Slf4j
public class SceneJob extends AbstractJob {
    @Override
    public void execute(JobExecutionContext context) {
        JobExecutionContextImpl jobExecutionContext = (JobExecutionContextImpl) context;
        Trigger trigger = jobExecutionContext.getTrigger();
        JobDataMap triggerJobDataMap = trigger.getJobDataMap();
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobDataMap detailJobDataMap = jobDetail.getJobDataMap();
        Calendar calendar = jobExecutionContext.getCalendar();
        log.info("triDataMap={},jobDataMap={},calendar={}", triggerJobDataMap, detailJobDataMap, calendar);
        log.info("job execute success");

    }
}
