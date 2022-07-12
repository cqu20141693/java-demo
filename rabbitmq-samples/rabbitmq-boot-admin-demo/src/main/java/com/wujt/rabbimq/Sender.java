package com.wujt.rabbimq;

import com.alibaba.fastjson.JSONObject;
import com.wujt.model.User;
import com.wujt.rabbimq.direct.ExchangeConfig;
import com.wujt.rabbimq.fanout.FanoutExchangeConfig;
import com.wujt.rabbimq.topic.TopicExchangeConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
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
public class Sender implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {


    private RabbitTemplate rabbitTemplate;

    @Autowired
    public Sender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
    }

    public void sendDirect(User msg) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        log.info("sendDirect: " + correlationData.getId());
        this.rabbitTemplate.convertAndSend(ExchangeConfig.DIRECT_EXCHANGE, ExchangeConfig.topicRoutingKey, JSONObject.toJSONString(msg), correlationData);
    }

    public void sendTopic(User msg) {
        String id = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(id);
        log.info("sendTopic: " + correlationData.getId());
        String message = JSONObject.toJSONString(msg);
        this.rabbitTemplate.convertAndSend(TopicExchangeConfig.TOPIC_EXCHANGE, "rabbitmq.spring.boot.cc", message, correlationData);
        CorrelationData correlationData1 = new CorrelationData(UUID.randomUUID().toString());
        this.rabbitTemplate.convertAndSend(TopicExchangeConfig.TOPIC_EXCHANGE, "rabbitmq.str.cc", message, correlationData1);

    }

    public void sendFanout(User msg) {
        String id = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(id);
        log.info("sendFanout: " + correlationData.getId());
        String message = JSONObject.toJSONString(msg);
        this.rabbitTemplate.convertAndSend(FanoutExchangeConfig.FANOUT_EXCHANGE, "", message, correlationData);
        CorrelationData correlationData1 = new CorrelationData(UUID.randomUUID().toString());
        this.rabbitTemplate.convertAndSend(FanoutExchangeConfig.FANOUT_EXCHANGE, "", message, correlationData1);

    }

    /**
     * 回调方法
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("confirm: " + correlationData.getId());
    }

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.info("retrun:{}", returned.toString());
    }
}

