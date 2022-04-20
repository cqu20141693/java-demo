package com.wujt.distributeTask.config;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.wujt.distributeTask.demo.DemoJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import static com.dangdang.ddframe.job.lite.config.LiteJobConfiguration.newBuilder;

@Configuration
public class DemoConfig {

    @Value("${schedule.demo.name}")
    private String demoJobName;

    @Value("${schedule.demo.cron}")
    private String demoJobCron;

    JobScheduler jobScheduler;

    @Autowired
    DemoJob demoJob;

    @Bean
    public JobScheduler demoJobScheduler(CoordinatorRegistryCenter zookeeperRegistryCenter, LiteJobConfiguration getLiteJobConfiguration) {
        MyElasticJobListener elasticJobListener = new MyElasticJobListener();
        jobScheduler = new SpringJobScheduler(demoJob, zookeeperRegistryCenter, getLiteJobConfiguration);
        jobScheduler.init();
        return jobScheduler;
    }

    @Bean
    public LiteJobConfiguration liteJobConfiguration() {
        return newBuilder(
                new SimpleJobConfiguration(
                        JobCoreConfiguration.newBuilder(demoJobName, demoJobCron, 2).build(), DemoJob.class.getCanonicalName()
                )).overwrite(true).build();
    }

    @Bean
    public JobEventConfiguration jobEventConfiguration() {
        DataSource dataSource = null;
        JobEventConfiguration jobEventConfiguration = new JobEventRdbConfiguration(dataSource);
        return jobEventConfiguration;
    }

    @Bean
    public LiteJobConfiguration getLiteJobConfiguration() {
        // 定义作业核心配置
        JobCoreConfiguration.Builder coreBuilder = JobCoreConfiguration.newBuilder(demoJobName, demoJobCron, 1);
        //是否开启失效转移
        coreBuilder.failover(true);
        //开启错过重新执行
        // coreBuilder.misfire(true);
        JobCoreConfiguration simpleCoreConfig = coreBuilder.build();
        // 定义SIMPLE类型配置
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, DemoJob.class.getCanonicalName());
        //设置是否failover
        // 定义Lite作业根配置
        LiteJobConfiguration.Builder builder = LiteJobConfiguration.newBuilder(simpleJobConfig);
        //true:可用于部署作业时，先禁止启动，部署结束后统一启动 ;默认false
        builder.disabled(false);
        //本地覆盖注册中心配置，每次启动作业都以本地配置为准
        builder.overwrite(true);
        //构建作业配置对象.
        LiteJobConfiguration simpleJobRootConfig = builder.build();
        //LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).overwrite(true).build();
        return simpleJobRootConfig;
    }

@PreDestroy
    public void dofinally(){
    }
}