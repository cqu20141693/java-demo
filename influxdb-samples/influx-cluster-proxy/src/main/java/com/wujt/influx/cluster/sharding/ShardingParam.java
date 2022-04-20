package com.wujt.influx.cluster.sharding;


import com.wujt.influx.cluster.TargetType;

/**
 * sharding规则执行所需参数
 *
 */
public class ShardingParam {
    private Long deviceId;

    private Long productId;

    private TargetType targetType = TargetType.data;

    public static ShardingParam create() {
        return new ShardingParam();
    }

    public static ShardingParam create(Long productId, Long deviceId) {
        return new ShardingParam().setProductId(productId).setDeviceId(deviceId);
    }

    public static ShardingParam create(Long productId, Long deviceId, TargetType targetType) {
        return new ShardingParam().setProductId(productId).setDeviceId(deviceId).setTargetType(targetType);
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public ShardingParam setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public Long getProductId() {
        return productId;
    }

    public ShardingParam setProductId(Long productId) {
        this.productId = productId;
        return this;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public ShardingParam setTargetType(TargetType targetType) {
        this.targetType = targetType;
        return this;
    }
}
