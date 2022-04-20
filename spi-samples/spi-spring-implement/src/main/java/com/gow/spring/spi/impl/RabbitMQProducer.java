package com.gow.spring.spi.impl;

import com.alibaba.fastjson.JSONObject;
import com.wujt.spi.MessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;

/**
 * @author gow
 * @date 2021/6/23
 */
@Slf4j
public class RabbitMQProducer implements MessageProducer, Ordered {
    @Override
    public <T> Boolean send(T t) {

       log.info("rabbitmq  send message={}", JSONObject.toJSON(t));
        return null;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
