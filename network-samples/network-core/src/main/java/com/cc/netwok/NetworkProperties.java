package com.cc.netwok;

import lombok.Data;

import java.util.Map;

/**
 * 网络组件配置信息
 * wcc 2022/4/26
 */
@Data
public class NetworkProperties {

    /**
     * 配置ID
     */
    private String id;

    /**
     * 配置名称
     */
    private String name;

    /**
     * 配置是否启用
     */
    private boolean enabled;

    /**
     * 配置内容，不同的网络组件，配置内容不同
     */
    private Map<String, Object> configurations;
}
