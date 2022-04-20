package com.wujt.influx;

/**
 * 读influxdb操作信息
 *
 * @author wujt
 */
public class InfluxReadOperation {
    /**
     * db名
     */
    private String database;

    /**
     * influxdb操作列表
     */
    private Influx influx;

    public static InfluxReadOperation create() {
        return new InfluxReadOperation();
    }

    public String getDatabase() {
        return database;
    }

    public InfluxReadOperation setDatabase(String database) {
        this.database = database;
        return this;
    }

    public Influx getInflux() {
        return influx;
    }

    public InfluxReadOperation setInflux(Influx influx) {
        this.influx = influx;
        return this;
    }
}
