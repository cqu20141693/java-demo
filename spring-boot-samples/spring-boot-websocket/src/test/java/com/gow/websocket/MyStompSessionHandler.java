package com.gow.websocket;

import com.gow.websocket.model.MessageBody;
import java.lang.reflect.Type;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

/**
 * @author gow
 * @date 2021/7/2 0002
 */
@Slf4j
public class MyStompSessionHandler implements StompSessionHandler {
    @Override
    public void afterConnected(
            StompSession session, StompHeaders connectedHeaders) {
        log.info("invoke getPayloadType afterConnected");
        session.subscribe("/topic", this);
        session.send("/app/test", getSampleMessage());
    }

    private Object getSampleMessage() {
        MessageBody messageBody = new MessageBody();
        messageBody.setContent("hello server");
        messageBody.setDestination("/topic");
        return messageBody;
    }

    @Override
    public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders,
                                byte[] bytes, Throwable throwable) {
        log.info("invoke getPayloadType handleException");
    }

    @Override
    public void handleTransportError(StompSession stompSession, Throwable throwable) {
        log.info("invoke getPayloadType handleTransportError");
    }

    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
        log.info("invoke getPayloadType method");
        return MessageBody.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {

        log.info("invoke handleFrame method,Received :{}", payload);
    }
}
