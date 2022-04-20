package com.wujt.config;

import org.springframework.amqp.support.converter.DefaultClassMapper;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/3/23
 */
public class RabbitMqFastJsonClassMapper extends DefaultClassMapper {
    /**
     * 构造函数初始化信任所有pakcage
     */
    public RabbitMqFastJsonClassMapper() {
        super();
        // 设置信任的包
        setTrustedPackages("*");
    }
}