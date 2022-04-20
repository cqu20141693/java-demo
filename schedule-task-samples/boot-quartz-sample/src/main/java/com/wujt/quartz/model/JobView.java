package com.wujt.quartz.model;

import lombok.Data;

import java.util.Map;

/**
 * @author wujt
 */
@Data
public class JobView {
    /**
     * 任务名称
     */
    private String jobName;
    /**
     * 任务分组
     */
    private String jobGroup;
    /**
     * 描述
     */
    private String description;

    /**
     * 是否对job 进行持久化
     */
    private Boolean durable = false;
    /**
     * 是否支持并行
     */
    private Boolean concurrent;
    /**
     * 是否支持job 失败被其他实例发现重新执行
     */
    private Boolean recovery;

    /**
     * 运行是参数
     */
    private Map<String, String> runTimeArgs;
}
