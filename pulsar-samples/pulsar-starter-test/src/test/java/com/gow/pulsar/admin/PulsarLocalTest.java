package com.gow.pulsar.admin;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.AuthenticationFactory;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.SubscriptionType;
import org.apache.pulsar.common.naming.NamespaceName;
import org.apache.pulsar.common.naming.TopicDomain;
import org.apache.pulsar.common.naming.TopicName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

/**
 * @author gow
 * @date 2021/8/30
 */
@Slf4j
public class PulsarLocalTest {


    private static final String serviceUrl = "pulsar://localhost:6650";
    private static final String webServiceUrl = "http://localhost:8080";
    private static final String superToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaG9uZ2N0ZWNoLXN1cGVyIn0.8g2EUfFzKtVKEeQzyLJWuVGDCNU4dY-hxciYZ_v--n8";
    private static final String adminToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjY3RlY2gifQ.TkCwKZIW-CCQEI6qjoAnEImpJzDymkYthWUymGQwArg";
    PulsarClient adminRoleClient = PulsarClient.builder().serviceUrl(serviceUrl)
            .authentication(AuthenticationFactory.token(adminToken))
            .statsInterval(60, TimeUnit.SECONDS)
            .ioThreads(1)
            .listenerThreads(1).build();

    PulsarClient superRoleClient = PulsarClient.builder().serviceUrl(serviceUrl)
            .authentication(AuthenticationFactory.token(superToken))
            .statsInterval(60, TimeUnit.SECONDS)
            .ioThreads(1)
            .listenerThreads(1).build();

    public PulsarLocalTest() throws PulsarClientException {
    }

    private static final String topicName =
            TopicName.get(TopicDomain.persistent.value(), "cloud", "subscribes", "test-partitioned-topic").toString();

    // first step: create tenant
    @Test
    @DisplayName("add Tenant")
    public void addTenant() throws PulsarClientException {
        TenantTest tenantTest = new TenantTest(webServiceUrl);
        tenantTest.createTenant("cloud");
    }

    // second step: create namespace
    @Test
    @DisplayName("add namespace")
    public void addNamespace() throws PulsarClientException {
        NamespaceTest namespaceTest = new NamespaceTest(webServiceUrl);
        NamespaceName namespaceName = NamespaceName.get("cloud", "subscribes");
        namespaceTest.createNamespace(namespaceName);
    }

    // if need partitioned topic ，create topic


    @Test
    @DisplayName("test standalone consumer")
    public void standAloneConsumer() throws PulsarClientException {
        Consumer<String> adminSubscribe = superRoleClient.newConsumer(Schema.STRING).topic(topicName)
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
    @DisplayName("test standalone producer")
    @RepeatedTest(4)
    public void standAloneProducer() throws PulsarClientException {

        Producer<String> stringProducer = superRoleClient.newProducer(Schema.STRING).topic(topicName).create();
        try {
            MessageId test = stringProducer.newMessage().key("test").value("test-gow-role-producer").send();
            log.info("gow-role-producer send success messageId={}", test.toString());
        } catch (PulsarClientException e) {
            e.printStackTrace();
        }
    }


    @Test
    @DisplayName("test")
    public void test() {

    }


}
