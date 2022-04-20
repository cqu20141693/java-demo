package com.wujt.influx.cluster.sharding;

/**
 *
 */
public class ShardingResult {
    /**
     * 库名
     */
    private String dataBase;

    /**
     * 过期策略
     */
    private String retentionPolicy;

    /**
     * sharding的bucket号
     */
    private Integer shardingBucket;

    /**
     * 无效写数据，忽略sharding
     */
    private boolean skipWrite = false;

    public String getDataBase() {
        return dataBase;
    }

    public void setDataBase(String dataBase) {
        this.dataBase = dataBase;
    }

    public String getRetentionPolicy() {
        return retentionPolicy;
    }

    public void setRetentionPolicy(String retentionPolicy) {
        this.retentionPolicy = retentionPolicy;
    }

    public Integer getShardingBucket() {
        return shardingBucket;
    }

    public void setShardingBucket(Integer shardingBucket) {
        this.shardingBucket = shardingBucket;
    }

    public boolean isSkipWrite() {
        return skipWrite;
    }

    public void setSkipWrite(boolean skipWrite) {
        this.skipWrite = skipWrite;
    }
}
