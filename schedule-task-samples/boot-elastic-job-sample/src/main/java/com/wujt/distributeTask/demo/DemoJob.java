package com.wujt.distributeTask.demo;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DemoJob implements SimpleJob {
    private Logger log= LoggerFactory.getLogger(DemoJob.class);
    @Override
    public void execute(ShardingContext shardingContext) {
        switch (shardingContext.getShardingItem()) {
            case 0:
               log.info("wujt:0");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("wujt:thread.sleep");
                break;
            case 1:
                log.info("wujt:1");
                break;
            default:
                log.info("wujt:default");
                break;
        }
    }
}