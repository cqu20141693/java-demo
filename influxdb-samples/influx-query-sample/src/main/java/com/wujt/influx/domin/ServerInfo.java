package com.wujt.influx.domin;

import com.wujt.influx.domin.schema.ServerInfoSchema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

/**
 * @author wujt
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Measurement(name = ServerInfoSchema.MEASUREMENT)
public class ServerInfo {
    /**
     * 时间戳
     */
    @Column(name = ServerInfoSchema.TIME)
    private Instant time;
    @Column(name = ServerInfoSchema.FILED_NAME)
    private String name;
    @Column(name = ServerInfoSchema.TAG_IP, tag = true)
    private String ip;
    // cpu 核心数
    @Column(name = ServerInfoSchema.TAG_CPUS, tag = true)
    private String cpu;
    // cpu 线程数
    @Column(name = ServerInfoSchema.TAG_THREAD, tag = true)
    private String thread;
    // 内存容量 GB
    @Column(name = ServerInfoSchema.TAG_MEMORY, tag = true)
    private String memory;
    // 带宽 MB
    @Column(name = ServerInfoSchema.TAG_BANDWIDTH, tag = true)
    private String bandwidth;
    // 当前进程数
    @Column(name = ServerInfoSchema.FILED_CURRENT_PROCESS)
    private Integer currentProcesses;
}
