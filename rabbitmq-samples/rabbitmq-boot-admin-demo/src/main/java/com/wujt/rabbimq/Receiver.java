package com.wujt.rabbimq;

import com.wujt.config.QueueConfig;

import com.wujt.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/3/23
 */
@Component
@Slf4j
public class Receiver {


    @RabbitListener(queues = QueueConfig.topicName)
    @RabbitHandler
    public void process(User foo) {
        log.info("Listener: " + foo);
    }
}