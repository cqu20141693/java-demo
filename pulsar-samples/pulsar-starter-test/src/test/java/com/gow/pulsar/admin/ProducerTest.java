package com.gow.pulsar.admin;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.AuthenticationFactory;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.common.naming.TopicDomain;
import org.apache.pulsar.common.naming.TopicName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author gow
 * @date 2021/8/25
 */
@Slf4j
public class ProducerTest {

    //    private static final String serviceUrl = "pulsar://localhost:6650";


    private static final String serviceUrl = "pulsar://172.30.203.25:6650,172.30.203.26:6650,172.30.203.24:6650";

    private static final String adminToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjY3RlY2gifQ.TkCwKZIW-CCQEI6qjoAnEImpJzDymkYthWUymGQwArg";

    private static final String gowToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJnb3cifQ.XEHjhJUU-yx6z-fOFid2K-Ez3EK-tlLUt4Ny0sk308A";

    private static final String subscribeRole = "";


    private static final String topicName =
            TopicName.get(TopicDomain.persistent.value(), "cloud", "subscribes", "test-partitioned-topic").toString();

  private final  PulsarClient adminRoleClient = PulsarClient.builder().serviceUrl(serviceUrl)
            .authentication(AuthenticationFactory.token(adminToken))
            .statsInterval(60, TimeUnit.SECONDS)
            .ioThreads(3)
            .listenerThreads(1).build();

//    PulsarClient subscribeRoleClient = PulsarClient.builder().serviceUrl(serviceUrl)
//            .authentication(AuthenticationFactory.token(subscribeRole))
//            .statsInterval(60, TimeUnit.SECONDS)
//            .ioThreads(1)
//            .listenerThreads(1).build();



    Producer<String> adminProducer = adminRoleClient.newProducer(Schema.STRING).topic(topicName).create();

//    Producer<String> gowProducer = gowRoleClient.newProducer(Schema.STRING).topic(topicName).create();

    public ProducerTest() throws PulsarClientException {
    }

    @Test
    @DisplayName("test testAdminRoleProducer")
    public void testAdminRoleProducer() {
        try {
            MessageId test = adminProducer.newMessage().key("test").value("test-admin-role-producer").send();
            log.info("admin-role-producer send success messageId={}", test.toString());
        } catch (PulsarClientException e) {
            e.printStackTrace();
        }

    }

    @Test
    @DisplayName("test gowRoleProducer")
    public void testGowRoleProducer() throws PulsarClientException {
        // must be config gow role  producer permission
        // 如果一个角色非admin角色或者是没有producer permission,将会没得权限进行分区获取
        PulsarClient gowRoleClient = PulsarClient.builder().serviceUrl(serviceUrl)
                .authentication(AuthenticationFactory.token(gowToken))
                .statsInterval(60, TimeUnit.SECONDS)
                .ioThreads(1)
                .listenerThreads(1).build();
        Producer<String> gowProducer = gowRoleClient.newProducer(Schema.STRING).topic(topicName).create();
        try {
            MessageId test = gowProducer.newMessage().key("test").value("test-gow-role-producer").send();
            log.info("gow-role-producer send success messageId={}", test.toString());
        } catch (PulsarClientException e) {
            e.printStackTrace();
        }

    }


}
