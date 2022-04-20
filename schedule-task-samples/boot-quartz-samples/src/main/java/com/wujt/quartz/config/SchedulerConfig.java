package com.wujt.quartz.config;

import org.quartz.Scheduler;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

/**
 * * 对quartz.rpoperties进行读取
 *
 * @author wujt
 */
@Configuration
public class SchedulerConfig {

    @Autowired
    private MyJobFactory myJobFactory;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();

        factory.setOverwriteExistingJobs(true);

        // 延时启动  让程序先启动后在进行调度；必须配置
        factory.setStartupDelay(5);

        // 加载quartz数据源配置
        factory.setQuartzProperties(quartzProperties());

        // 可以自己配置SchedulerFactory 从而实现创建Scheduler；
        // more使用
        // factory.setSchedulerFactory(schedulerFactory);

        // 自定义Job Factory，用于Spring注入
        factory.setJobFactory(myJobFactory);

        // todo 对spring boot starter 中注入的Scheduler，Job,Trigger进行控制；
        return factory;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz1.properties"));
        //在quartz.properties中的属性被读取并注入后再初始化对象
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    /*
     * quartz初始化监听器
     */
    @Bean
    public QuartzInitializerListener executorListener() {
        return new QuartzInitializerListener();
    }

    /*
     * 通过SchedulerFactoryBean获取Scheduler的实例
     */
    @Bean(name = "scheduler")
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) throws Exception {
        //使用SchedulerFactoryBean 获取Scheduler的好处是单例
        // 如果要实现多个Scheduler 需要使用SchedulerFactory 进行创建
        return schedulerFactoryBean.getScheduler();
    }

}
