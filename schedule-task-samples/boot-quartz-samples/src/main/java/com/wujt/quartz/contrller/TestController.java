package com.wujt.quartz.contrller;

import com.wujt.quartz.domain.PowerYourselfJob;
import com.wujt.quartz.service.JobAndTriggerServiceImpl;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wujt
 */
@RestController
@RequestMapping("quartz")
public class TestController {

    @Autowired
    private JobAndTriggerServiceImpl jobAndTriggerService;

    @RequestMapping("/test")
    public String test() {
        PowerYourselfJob job = new PowerYourselfJob();
        job.setJobName("111111111");
        job.setJobGroup("2222222222");
        job.setDescription("3333333333");
        JobDataMap dataMap=new JobDataMap();
        dataMap.put("data","hello");
        job.setDataMap(dataMap);
        //Integer[] date = DateParseForCronExpressionUtil.parse(jobFormVo.getRemindTime());
        job.setCronExpression(
                "*/5 * * * * ?"
        );
        try {
            jobAndTriggerService.addJob(job);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return "success";
    }
}