package com.wujt.rabbimq;

import com.wujt.model.User;
import com.wujt.rabbimq.direct.ExchangeConfig;
import com.wujt.rabbimq.exchange.QueueConfig;
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

    /**
     * direct routingKey 为queue
     * @param foo
     */
    @RabbitListener(queues = ExchangeConfig.routingKey)
    @RabbitHandler
    public void processDirect(String foo) {
        log.info("Listener: " + foo);
    }

    @RabbitListener(queues = QueueConfig.USER_QUEUE)
    @RabbitHandler
    public void processUser(String foo) {
        log.info("Listener: " + foo);
    }


    @RabbitListener(queues = QueueConfig.STRING_QUEUE)
    @RabbitHandler
    public void process(String foo) {
        log.info("Listener: " + foo);
    }
}
