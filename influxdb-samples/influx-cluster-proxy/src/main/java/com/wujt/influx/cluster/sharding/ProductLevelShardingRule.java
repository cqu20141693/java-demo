package com.wujt.influx.cluster.sharding;

import com.wujt.influx.cluster.TargetType;
import com.wujt.influx.config.InfluxDbConfig;
import com.wujt.influx.config.rule.ProductRuleConfig;
import com.wujt.influx.config.rule.RuleData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 产品级，对不同产品入的库进行区分
 *
 * @author zc
 * @date 2019/7/31 17:37
 */
@Component
@ConditionalOnBean(value = {InfluxDbConfig.class})
public class ProductLevelShardingRule implements ShardingRule {
    private final static Logger logger = LoggerFactory.getLogger(ProductLevelShardingRule.class);

    @Autowired
    private ProductRuleConfig productRuleConfig;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void exec(ShardingParam shardingParam, ShardingResult shardingResult) {
        if (shardingParam.getTargetType().equals(TargetType.data)) {
            logger.debug("exec ProductLevelSharding");
            Map<Long, RuleData> ruleDataMap = productRuleConfig.getRuleData();
            logger.debug("ruleDataMap ： {}", ruleDataMap);
            if (ruleDataMap != null && ruleDataMap.containsKey(shardingParam.getProductId())) {
                RuleData productRuleData = ruleDataMap.get(shardingParam.getProductId());
                shardingResult.setDataBase(productRuleData.getDataBase());
                shardingResult.setRetentionPolicy(productRuleData.getRetentionPolicy());
                shardingResult.setSkipWrite(productRuleData.getFactor() == 1);
                shardingResult.setShardingBucket(productRuleData.getSpecialBucketNum());
            }
        }
    }
}
