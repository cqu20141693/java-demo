package com.wujt.influx.domin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wujt
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServerInfo {
    /**
     * 时间戳
     */
    private long time;
    private String name;
    private String ip;
    // cpu 核心数
    private Integer cpu;
    // cpu 线程数
    private Integer thread;
    // 内存容量 GB
    private Integer memory;
    // 带宽 MB
    private Integer bandwidth;
    // 当前进程数
    private Integer currentProcesses;
}
