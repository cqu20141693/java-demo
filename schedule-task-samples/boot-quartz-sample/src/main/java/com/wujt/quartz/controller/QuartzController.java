package com.wujt.quartz.controller;

import com.wujt.quartz.domain.SceneJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wujt
 */
@RestController
@RequestMapping("quartz")
public class QuartzController {

    @Autowired
    private Scheduler scheduler;

    /**
     * todo 思考
     * 将执行代码和job 捆绑到一起： quartz 天然支持
     * 将执行代码和job 解耦： 借用XXL-job思维
     * 实现一个HttpServer 作为执行器的 :
     * 当执行job 时需要到数据库中查询可执行的执行器服务器地址：
     * 间接的需要新增表：Job:执行器bean:执行器服务地址
     * 然后将上下问参数和执行器bean传入执行器服务器：
     * 执行器服务器收到请求后实例化执行器bean或是取出bean；然后执行bean的task方法；
     * 好处是： 当我们的job执行代码需要改动的时候；只需要修改执行器端代码；然后实现滚动更新；
     * 如果是quartz 方式： 则需要直接修改程序代码；并重新启动调度器代码；风险比较高；
     *
     * @param sceneJob
     * @return
     */
    @PostMapping("addJob")
    public Object addJob(@RequestBody SceneJob sceneJob) throws SchedulerException {
        /** 创建JobDetail实例,绑定Job实现类
         * JobDetail 表示一个具体的可执行的调度程序,job是这个可执行调度程序所要执行的内容
         * 另外JobDetail还包含了这个任务调度的方案和策略**/
        // 指明job的名称，所在组的名称，以及绑定job类
        JobDataMap dataMap = new JobDataMap();
        sceneJob.getRunTimeArgs().forEach(dataMap::put);
        JobDetail jobDetail = JobBuilder.newJob(sceneJob.getClass())
                .withIdentity(sceneJob.getJobName(), sceneJob.getJobGroup())
                .withDescription(sceneJob.getDescription())
                .usingJobData(dataMap)
                .requestRecovery(sceneJob.getRecovery())
                .storeDurably(sceneJob.getDurable())
                .build();

        scheduler.addJob(jobDetail, false);

        return null;
    }

    @PostMapping("addCronTrigger")
    public void addTrigger(String name, String group, String cron, String calName) {
        SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().build();
        // cron 触发器只需要name,group,cron，calName
        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(name, group)
                .modifiedByCalendar(calName)
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();
//        scheduler.resumeTrigger();
    }

    /**todo  scheduler API使用
     *   设计任务的添加接口；触发器的添加接口，日历的添加接口
     *   启动触发器的接口；删除触发器接口，删除任务接口；
     *   查询任务；查询触发器接口 。。。
     *   实现监控和界面定义；
     */

}
