package com.wujt.influx.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@ConfigurationProperties(prefix = "extend.influx.schedule")
@EnableScheduling
@ConditionalOnProperty(prefix = "wujt.influxdb.proxy", value = "enable", havingValue = "true", matchIfMissing = true)
public class ScheduleConfig implements SchedulingConfigurer {

    /**
     * 调度池大小，可配置
     */
    private int corePoolSize = 2;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(scheduleTaskExecutor());
    }

    @Bean(destroyMethod = "shutdown")
    public ExecutorService scheduleTaskExecutor() {
        return Executors.newScheduledThreadPool(corePoolSize);
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }
}
