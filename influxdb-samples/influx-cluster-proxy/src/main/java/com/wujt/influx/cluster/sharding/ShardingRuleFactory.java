package com.wujt.influx.cluster.sharding;

import com.wujt.influx.config.InfluxDbConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;

/**
 * 分片抽象集合工厂，一期暂时不实现
 */
@Component
@ConditionalOnBean(value = {InfluxDbConfig.class})
public class ShardingRuleFactory {
    @Autowired
    private InfluxDbConfig influxDbConfig;

    @Autowired
    private List<ShardingRule> shardingRuleList;

    @PostConstruct
    public void init() {
        // 保证优先级高的在前
        shardingRuleList.sort(Comparator.comparingInt(ShardingRule::getOrder));
    }

    public ShardingResult execAllWriteRule(ShardingParam shardingParam) {
        ShardingResult shardingResult = getDefaultShardingResult(shardingParam);
        for (ShardingRule shardingRule : shardingRuleList) {
            shardingRule.exec(shardingParam, shardingResult);
            if (shardingResult.isSkipWrite()) {
                //无效写数据，直接结束
                return shardingResult;
            }
        }
        return shardingResult;
    }

    public ShardingResult execAllReadRule(ShardingParam shardingParam) {
        ShardingResult shardingResult = getDefaultShardingResult(shardingParam);
        for (ShardingRule shardingRule : shardingRuleList) {
            shardingRule.exec(shardingParam, shardingResult);
        }
        return shardingResult;
    }

    private ShardingResult getDefaultShardingResult(ShardingParam shardingParam) {
        ShardingResult shardingResult = new ShardingResult();
        switch (shardingParam.getTargetType()) {
            case data:
                //设置默认值
                shardingResult.setDataBase(influxDbConfig.getDefaultDataDbName());
                shardingResult.setRetentionPolicy(influxDbConfig.getDefaultDataRetentionPolicy());
                shardingResult.setShardingBucket(ShardingDefault.DEFAULT_BUCKET_NUM);
                break;
            case event_info:
                shardingResult.setDataBase(influxDbConfig.getDefaultEventInfoDbName());
                shardingResult.setRetentionPolicy(influxDbConfig.getDefaultEventInfoRetentionPolicy());
                shardingResult.setShardingBucket(ShardingDefault.DEFAULT_BUCKET_NUM);
                break;
            case sys_log:
                shardingResult.setDataBase(influxDbConfig.getDefaultSysLogDbName());
                shardingResult.setRetentionPolicy(influxDbConfig.getDefaultSysLogRetentionPolicy());
                shardingResult.setShardingBucket(ShardingDefault.DEFAULT_BUCKET_NUM);
                break;
            default:
        }
        return shardingResult;
    }
}
