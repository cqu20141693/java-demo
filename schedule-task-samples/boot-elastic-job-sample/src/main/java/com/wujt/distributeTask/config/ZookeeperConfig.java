package com.wujt.distributeTask.config;

import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZookeeperConfig {
    @Value("${zk.servers}")
    private String serviceLists;

    @Value("${zk.namespace}")
    private String nameSpace;

    @Value("${zk.baseSleepTimeMilliseconds}")
    private int baseSleepTimeMilliseconds;

    @Value("${zk.maxSleepTimeMilliseconds}")
    private int maxSleepTimeMilliseconds;

    @Value("${zk.maxRetries}")
    private int maxRetries;

    /**
     * zookeeper 配置
     * @return
     */
    @Bean
    public CoordinatorRegistryCenter zookeeperRegistryCenter(){
        ZookeeperConfiguration configuration = new ZookeeperConfiguration(serviceLists,nameSpace);
        configuration.setBaseSleepTimeMilliseconds(baseSleepTimeMilliseconds);
        configuration.setMaxSleepTimeMilliseconds(maxSleepTimeMilliseconds);
        configuration.setMaxRetries(maxRetries);
        CoordinatorRegistryCenter coordinatorRegistryCenter=new ZookeeperRegistryCenter(configuration);
        coordinatorRegistryCenter.init();
        return coordinatorRegistryCenter;
    }
}