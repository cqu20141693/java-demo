package com.wujt.influx.config.rule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * <p>
 * 由dataId : influx-proxy-core.yml定义配置
 * 使用的系统需引入
 */
@Configuration
@ConfigurationProperties(prefix = "extend.influx.rule.product")
@RefreshScope
@ConditionalOnProperty(prefix = "wujt.influxdb.proxy", value = "enable", havingValue = "true", matchIfMissing = true)
public class ProductRuleConfig {
    private final static Logger logger = LoggerFactory.getLogger(ProductRuleConfig.class);
    /**
     * 规则数据，productId -> ruleData
     */
    private volatile Map<Long, RuleData> ruleData;

    /**
     * 配置
     */
    private volatile boolean hasBeenConfig = false;

    public Map<Long, RuleData> getRuleData() {
        return ruleData;
    }

    public void setRuleData(Map<Long, RuleData> ruleData) {
        logger.info("update product rule config : {}", ruleData);
        this.ruleData = ruleData;
        if (!hasBeenConfig) {
            hasBeenConfig = true;
        }
    }

    public boolean isHasBeenConfig() {
        return hasBeenConfig;
    }
}
