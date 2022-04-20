package com.gow.pulsar.test.consumer;

import com.gow.pulsar.core.domain.PulsarProperties;
import com.gow.pulsar.listener.IMessageListener;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.SubscriptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/7
 */
@Component
public class MessageListenerConsumer implements ApplicationRunner {

    @Autowired
    private PulsarClient pulsarClient;

    @Autowired
    private PulsarProperties properties;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        startListener();
    }

    private void startListener() throws PulsarClientException {
        IMessageListener<String> messageListener = new IMessageListener();
        pulsarClient.newConsumer(Schema.STRING)
                .topic("test")
                .messageListener(messageListener)
                .subscriptionName("msg-listener-consumer")
                .subscriptionType(SubscriptionType.Key_Shared)
                .subscribe();
    }
}
