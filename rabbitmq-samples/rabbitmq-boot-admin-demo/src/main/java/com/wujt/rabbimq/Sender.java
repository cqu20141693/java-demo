package com.wujt.rabbimq;

import com.wujt.config.ExchangeConfig;
import com.wujt.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/3/23
 */
@Component
@Slf4j
public class Sender implements RabbitTemplate.ConfirmCallback {


    private RabbitTemplate rabbitTemplate;

    @Autowired
    public Sender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
    }

    public void send(User msg) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        log.info("send: " + correlationData.getId());
        this.rabbitTemplate.convertAndSend(ExchangeConfig.exchangName, ExchangeConfig.topicRoutingKey, msg, correlationData);
    }

    /**
     * 回调方法
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("confirm: " + correlationData.getId());
    }
}

