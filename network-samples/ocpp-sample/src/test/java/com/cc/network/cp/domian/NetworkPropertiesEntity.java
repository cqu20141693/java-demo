package com.cc.network.cp.domian;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 网络配置信息
 * wcc 2022/4/26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NetworkPropertiesEntity {
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
