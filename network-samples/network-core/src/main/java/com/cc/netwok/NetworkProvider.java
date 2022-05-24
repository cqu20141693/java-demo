package com.cc.netwok;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 网络组件提供商
 * wcc 2022/4/26
 */
public interface NetworkProvider<P> {
    /**
     * @return 类型
     * @see DefaultNetworkType
     */
    @Nonnull
    NetworkType getType();

    /**
     * 使用配置创建一个网络组件
     *
     * @param properties 配置信息
     * @return 网络组件
     */
    @Nonnull
    Network createNetwork(@Nonnull P properties);

    /**
     * 重新加载网络组件
     *
     * @param network    网络组件
     * @param properties 配置信息
     */
    void reload(@Nonnull Network network, @Nonnull P properties);

    /**
     * @return 配置定义元数据
     */
    @Nullable
    ConfigMetadata getConfigMetadata();

    /**
     * 根据可序列化的配置信息创建网络组件配置
     *
     * @param properties 原始配置信息
     * @return 网络配置信息
     */
    @Nonnull
    P createConfig(@Nonnull NetworkProperties properties);

}
