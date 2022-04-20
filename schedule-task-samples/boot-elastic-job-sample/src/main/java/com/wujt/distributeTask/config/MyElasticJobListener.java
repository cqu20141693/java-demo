package com.wujt.distributeTask.config;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyElasticJobListener implements ElasticJobListener {
    private Logger log= LoggerFactory.getLogger(MyElasticJobListener.class);
    private long beginTime = 0;
    @Override
    public void beforeJobExecuted(ShardingContexts shardingContexts) {
        beginTime = System.currentTimeMillis();

        log.info("{} JOB BEGIN TIME: {} ",shardingContexts.getJobName(), System.currentTimeMillis());
    }

    @Override
    public void afterJobExecuted(ShardingContexts shardingContexts) {
        log.info("{} JOB END TIME: {},TOTAL CAST: {}",shardingContexts.getJobName(),System.currentTimeMillis());
    }


}