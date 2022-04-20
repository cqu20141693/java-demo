package com.wujt.influx.config.rule;

/**
 *
 */
public class RuleData {
    /**
     * 配置指定db
     */
    private String dataBase;

    /**
     * 配置指定过期策略
     */
    private String retentionPolicy;

    /**
     * 产品特殊标记 , 1标识忽略产品
     */
    private Integer factor = 0;

    /**
     * 特殊bucket号，设置则使用指定的influxdb，需要和对应的服务器配置一致
     */
    private Integer specialBucketNum = -1;

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

    public Integer getFactor() {
        return factor;
    }

    public void setFactor(Integer factor) {
        this.factor = factor;
    }

    public Integer getSpecialBucketNum() {
        return specialBucketNum;
    }

    public void setSpecialBucketNum(Integer specialBucketNum) {
        this.specialBucketNum = specialBucketNum;
    }

    @Override
    public String toString() {
        return "RuleData{" +
                "dataBase='" + dataBase + '\'' +
                ", retentionPolicy='" + retentionPolicy + '\'' +
                ", factor=" + factor +
                ", specialBucketNum=" + specialBucketNum +
                '}';
    }
}
