package com.wujt.influx.domin.schema;

/**
 * @author wujt
 */
public class ServerInfoSchema {
    public static final String MEASUREMENT = "server_info";
    public static final String TIME = "time";
    public static final String TIME_START = "start";
    public static final String TIME_END = "end";
    public static final String TAG_IP = "ip";
    public static final String TAG_CPUS = "cpu";
    public static final String TAG_MEMORY = "memory";
    public static final String TAG_BANDWIDTH = "bandwidth";
    public static final String TAG_THREAD = "thread";
    public static final String FILED_NAME = "name";
    public static final String FILED_CURRENT_PROCESS = "current_process";
    public static final String COUNT_OUTPUT = "count_output";
}
