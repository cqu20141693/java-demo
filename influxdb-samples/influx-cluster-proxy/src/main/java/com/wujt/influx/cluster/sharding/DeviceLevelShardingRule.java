package com.wujt.influx.cluster.sharding;

import com.wujt.influx.config.InfluxDbConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * @author zc
 * @date 2019/7/31 18:49
 */
@Component
@ConditionalOnBean(value = {InfluxDbConfig.class})
public class DeviceLevelShardingRule implements ShardingRule {
    private final static Logger logger = LoggerFactory.getLogger(DeviceLevelShardingRule.class);

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void exec(ShardingParam shardingParam, ShardingResult shardingResult) {
        logger.debug("exec DeviceLevelSharding");
        if (shardingResult.getShardingBucket() <= 0) {
            //deviceId <0 则要拿产品级的bucket ,会有多个bucket
            //当前不分，都是第一个
            shardingResult.setShardingBucket(1);
        }
    }
}
