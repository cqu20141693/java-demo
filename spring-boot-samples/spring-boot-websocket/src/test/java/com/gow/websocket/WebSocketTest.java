package com.gow.websocket;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

/**
 * @author gow
 * @date 2021/7/2 0002
 */
public class WebSocketTest {
    private static final String URL = "ws://localhost:8080/health";

    public static void main(String[] args) {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));

        SockJsClient sockjsClient = new SockJsClient(transports);

        WebSocketStompClient stompClient = new WebSocketStompClient(sockjsClient);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        stompClient.setTaskScheduler(new ConcurrentTaskScheduler());

        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        stompClient.connect("ws://localhost:8080/health", sessionHandler);

        new Scanner(System.in).nextLine();
    }
}
