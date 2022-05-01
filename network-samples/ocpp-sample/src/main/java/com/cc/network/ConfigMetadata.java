package com.cc.network;

import java.util.List;

/**
 * wcc 2022/4/26
 */
public interface ConfigMetadata {

    /**
     * @return 配置名称
     */
    String getName();

    /**
     * @return 配置说明
     */
    String getDescription();

    /**
     * @return 配置属性信息
     */
    List<ConfigPropertyMetadata> getProperties();

    /**
     * 复制为新的配置,并按指定的scope过滤属性,只返回符合scope的属性.
     *
     * @param scopes 范围
     * @return 新的配置
     */
    ConfigMetadata copy(ConfigScope... scopes);
}
