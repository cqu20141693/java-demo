package com.gow.pulsar.admin;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.common.naming.TopicDomain;
import org.apache.pulsar.common.naming.TopicName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author gow
 * @date 2021/8/25
 */
@Slf4j
public class ConsumerTest {
    //    private static final String serviceUrl = "pulsar://localhost:6650";

    private static final String serviceUrl = "pulsar://172.30.203.25:6650,172.30.203.26:6650,172.30.203.24:6650";

    private static final String adminToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjY3RlY2gifQ.TkCwKZIW-CCQEI6qjoAnEImpJzDymkYthWUymGQwArg";

    private static final String gowToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJnb3cifQ.XEHjhJUU-yx6z-fOFid2K-Ez3EK-tlLUt4Ny0sk308A";

    private static final String subscribeRoleToken = "";

    private static final String topicName =
            TopicName.get(TopicDomain.persistent.value(), "cloud", "subscribes", "test-partitioned-topic").toString();

    PulsarClient adminRoleClient = PulsarClient.builder().serviceUrl(serviceUrl)
            .authentication(AuthenticationFactory.token(adminToken))
            .statsInterval(60, TimeUnit.SECONDS)
            .ioThreads(3)
            .listenerThreads(1).build();

    PulsarClient subscribeRoleClient = PulsarClient.builder().serviceUrl(serviceUrl)
            .authentication(AuthenticationFactory.token(subscribeRoleToken))
            .statsInterval(60, TimeUnit.SECONDS)
            .ioThreads(1)
            .listenerThreads(1).build();

    PulsarClient gowRoleClient = PulsarClient.builder().serviceUrl(serviceUrl)
            .authentication(AuthenticationFactory.token(gowToken))
            .statsInterval(60, TimeUnit.SECONDS)
            .ioThreads(1)
            .listenerThreads(1).build();


    public ConsumerTest() throws PulsarClientException {


    }


    @Test
    @DisplayName(("test adminRoleConsumer"))
    public void testAdminRoleConsumer() throws PulsarClientException {
        Consumer<String> adminSubscribe = adminRoleClient.newConsumer(Schema.STRING)
                .topic(topicName)
                .subscriptionName("admin-role-subscription")
                .subscriptionType(SubscriptionType.Exclusive)
                .subscribe();

        log.info("start testAdminRoleConsumer topic={}", topicName);
        while (true) {
            try {
                Message<String> receive = adminSubscribe.receive();
                log.info("receive msg={}", receive.getValue());
                adminSubscribe.acknowledge(receive);
            } catch (PulsarClientException e) {
                log.error("adminSubscribe receive msg failed,e={} ", e.getMessage());
            }
        }
    }

    @Test
    @DisplayName(("test testGowRoleConsumer"))
    public void testGowRoleConsumer() throws PulsarClientException {
        // gow role is not the topic admin role
        // 需要利用admin role进行topic consume permission 授权
        log.info("start testGowRoleConsumer topic={}", topicName);
        Consumer<String> gowSubscribe = gowRoleClient.newConsumer(Schema.STRING)
                .topic(topicName)
                .subscriptionName("gow-role-subscription")
                .subscriptionType(SubscriptionType.Failover)
                .subscribe();

        while (true) {
            try {
                Message<String> receive = gowSubscribe.receive();
                log.info("gowSubscribe receive msg={}", receive.getValue());
                gowSubscribe.acknowledge(receive);
            } catch (PulsarClientException e) {
                log.error("adminSubscribe receive msg failed,e={} ", e.getMessage());
            }
        }
    }
    @Test
    @DisplayName(("test testGowRoleConsumer2"))
    public void testGowRoleConsumer2() throws PulsarClientException {
        // gow role is not the topic admin role
        // 需要利用admin role进行topic consume permission 授权
        log.info("start testGowRoleConsumer topic={}", topicName);
        Consumer<String> gowSubscribe = gowRoleClient.newConsumer(Schema.STRING)
                .topic(topicName)
                .subscriptionName("gow-role-subscription")
                .subscriptionType(SubscriptionType.Failover)
                .subscribe();

        while (true) {
            try {
                Message<String> receive = gowSubscribe.receive();
                log.info("gowSubscribe receive msg={}", receive.getValue());
                gowSubscribe.acknowledge(receive);
            } catch (PulsarClientException e) {
                log.error("adminSubscribe receive msg failed,e={} ", e.getMessage());
            }
        }
    }

    @Test
    @DisplayName(("test adminRoleConsumer terminate"))
    public void adminRoleConsumer() throws PulsarClientException {
        TopicName terminate = TopicName.get(TopicDomain.persistent.value(), "cloud", "subscribes", "terminate");;
        Consumer<String> adminSubscribe = adminRoleClient.newConsumer(Schema.STRING)
                .topic(terminate.toString())
                .subscriptionName("admin-role-subscription")
                .subscriptionType(SubscriptionType.Exclusive)
                .subscribe();

        log.info("start testAdminRoleConsumer topic={}", terminate.toString());
        while (true) {
            try {
                Message<String> receive = adminSubscribe.receive();
                log.info("receive msg={}", receive.getValue());
                adminSubscribe.acknowledge(receive);
            } catch (PulsarClientException e) {
                log.error("adminSubscribe receive msg failed,e={} ", e.getMessage());
            }
        }
    }
}
