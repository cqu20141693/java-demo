package com.wujt.influx;

import java.util.List;

/**
 * @author wujt
 * 写influxdb操作信息
 */
public class InfluxWriteOperation {
    /**
     * db名
     */
    private String database;

    /**
     * retention策略，对应db的默认策略
     */
    private String retentionPolicy;

    /**
     * influxdb操作列表
     */
    private List<Influx> influxList;

    public static InfluxWriteOperation create() {
        return new InfluxWriteOperation();
    }

    public String getDatabase() {
        return database;
    }

    public InfluxWriteOperation setDatabase(String database) {
        this.database = database;
        return this;
    }

    public String getRetentionPolicy() {
        return retentionPolicy;
    }

    public InfluxWriteOperation setRetentionPolicy(String retentionPolicy) {
        this.retentionPolicy = retentionPolicy;
        return this;
    }

    public List<Influx> getInfluxList() {
        return influxList;
    }

    public InfluxWriteOperation setInfluxList(List<Influx> influxList) {
        this.influxList = influxList;
        return this;
    }
}
