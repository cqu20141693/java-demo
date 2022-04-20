package com.wujt.influx.cluster.sharding;

/**
 * 定义分片规则抽象
 */
public interface ShardingRule {
    /**
     * 规则优先级,fan
     *
     * @return
     */
    int getOrder();

    /**
     * 执行规则
     *
     * @return
     */
    void exec(ShardingParam shardingParam, ShardingResult shardingResult);
}
