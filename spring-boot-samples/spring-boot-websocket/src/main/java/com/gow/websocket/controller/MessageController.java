package com.gow.websocket.controller;

import com.gow.websocket.model.Greeting;
import com.gow.websocket.model.HelloMessage;
import com.gow.websocket.model.MessageBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

/**
 * @author gow
 * @date 2021/7/2 0002
 */
@Controller
@Slf4j
public class MessageController {
    /**
     * 消息发送工具对象
     */
    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        log.info("invoked greeting method");
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    /**
     * 广播发送消息，将消息发送到指定的目标地址
     */
    @MessageMapping("/test")
    public void sendTopicMessage(MessageBody messageBody) {
            log.info("invoked test method");
        // 将消息发送到 WebSocket 配置类中配置的代理中（/topic）进行消息转发
        simpMessageSendingOperations.convertAndSend(messageBody.getDestination(), messageBody);
    }
}
