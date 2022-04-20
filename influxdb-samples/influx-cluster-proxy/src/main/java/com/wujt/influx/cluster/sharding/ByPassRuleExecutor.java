package com.wujt.influx.cluster.sharding;

import com.wujt.influx.cluster.TargetType;
import com.wujt.influx.config.InfluxDbConfig;
import com.wujt.influx.config.rule.ByPassRuleConfig;
import com.wujt.influx.config.rule.RuleData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 */
@Component
@ConditionalOnBean(value = {InfluxDbConfig.class})
public class ByPassRuleExecutor {
    private final static Logger logger = LoggerFactory.getLogger(ByPassRuleExecutor.class);

    @Autowired
    private ByPassRuleConfig byPassRuleConfig;

    public ShardingResult execByPassRule(ShardingParam shardingParam) {
        if (shardingParam.getTargetType().equals(TargetType.data)) {
            Map<Long, RuleData> ruleDataMap = byPassRuleConfig.getRuleData();
            logger.debug("bypass ruleDataMap ： {}", ruleDataMap);
            if (ruleDataMap != null && ruleDataMap.containsKey(shardingParam.getProductId())) {
                RuleData byPassRuleData = ruleDataMap.get(shardingParam.getProductId());
                ShardingResult shardingResult = new ShardingResult();
                shardingResult.setDataBase(byPassRuleData.getDataBase());
                shardingResult.setRetentionPolicy(byPassRuleData.getRetentionPolicy());
                shardingResult.setShardingBucket(byPassRuleData.getSpecialBucketNum() <= 0 ? 1 : byPassRuleData.getSpecialBucketNum());
                return shardingResult;
            }
        }
        return null;
    }
}
