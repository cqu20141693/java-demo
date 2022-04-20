package com.gow.pulsar.admin;

import com.alibaba.fastjson.JSONObject;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.Namespaces;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.AuthenticationFactory;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.SubscriptionType;
import org.apache.pulsar.common.naming.NamespaceName;
import org.apache.pulsar.common.policies.data.AutoSubscriptionCreationOverride;
import org.apache.pulsar.common.policies.data.Policies;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author gow
 * @date 2021/8/25
 */
@Slf4j
public class NamespaceTest {
    //    private static final String webServiceUrl = "http://localhost:8080";

    private static final String webServiceUrl = "http://172.30.203.25:8080,172.30.203.26:8080,172.30.203.24:8080";
    private static final String superToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaG9uZ2N0ZWNoLXN1cGVyLXVzZXIifQ"
                    + ".o8GzC_pMaIayEJJxwcfQsTLRuup6eL0idBFgiSJLjmo";
    PulsarAdmin admin;

    public NamespaceTest() throws PulsarClientException {
        admin = PulsarAdmin.builder()
                .authentication(AuthenticationFactory.token(superToken))
                .serviceHttpUrl(webServiceUrl)
                .build();
    }
    public NamespaceTest(String webServiceUrl) throws PulsarClientException {

        admin = PulsarAdmin.builder()
                .authentication(AuthenticationFactory.token(superToken))
                .serviceHttpUrl(webServiceUrl)
                .build();
    }
    private static final String tenant = "cloud";


    @Test
    @DisplayName("test getNamespaceInfo")
    public void getNamespaceInfo() {

        Namespaces namespaces = admin.namespaces();
        try {
            List<String> list = namespaces.getNamespaces(tenant);
            log.info("tenant={} has {} namespaces", tenant, list.toString());
        } catch (PulsarAdminException pulsarAdminException) {
            pulsarAdminException.printStackTrace();
        }

    }

    @Test
    @DisplayName("test createNamespaceInfo")
    public void createNamespaceInfo() {
        NamespaceName namespaceName = NamespaceName.get(tenant, "subscribes");
        createNamespace(namespaceName);
    }

    public void createNamespace(NamespaceName namespaceName) {
        Namespaces namespaces = admin.namespaces();
        try {
            List<String> list = namespaces.getNamespaces(tenant);
            log.info("tenant={} has {} namespace", tenant, list.toString());

            if (list.contains(namespaceName.toString())) {
                log.info("namespace={} exist", namespaceName.toString());
            } else {
                log.info("namespace={} not exist ", namespaceName.toString());

                namespaces.createNamespace(namespaceName.toString());
            }
        } catch (PulsarAdminException pulsarAdminException) {
            pulsarAdminException.printStackTrace();
        }
    }


    @Test
    @DisplayName("test getPolicies")
    public void getPolicies() {

        Namespaces namespaces = admin.namespaces();
        try {
            List<String> list = namespaces.getNamespaces(tenant);
            log.info("tenant={} has {} namespace", tenant, list.toString());
            NamespaceName namespaceName = NamespaceName.get(tenant, "subscribes");
            if (list.contains(namespaceName.toString())) {
                log.info("namespace={} exist", namespaceName.toString());
                Policies policies = namespaces.getPolicies(namespaceName.toString());
                log.info("namespace={} has policies={}", namespaceName.toString(), JSONObject.toJSONString(policies));
            } else {
                log.info("namespace={} not exist", namespaceName.toString());

                namespaces.createNamespace(namespaceName.toString());
            }
        } catch (PulsarAdminException pulsarAdminException) {
            pulsarAdminException.printStackTrace();
        }
    }

    @Test
    @DisplayName("test getTopics")
    public void getTopics() {

        Namespaces namespaces = admin.namespaces();
        try {
            List<String> list = namespaces.getNamespaces(tenant);
            log.info("tenant={} has {} namespace", tenant, list.toString());
            String namespace = "subscribes";
            NamespaceName namespaceName = NamespaceName.get(tenant, namespace);
            if (list.contains(namespace)) {
                log.info("namespace={} exist", namespaceName.toString());
                List<String> topics = namespaces.getTopics(namespaceName.toString());
                log.info("namespace has topics={}", namespaceName.toString(), JSONObject.toJSONString(topics));
            } else {
                log.info("namespace={} not exist on tenant={}", namespace, tenant);

                namespaces.createNamespace(namespaceName.toString());
            }
        } catch (PulsarAdminException pulsarAdminException) {
            pulsarAdminException.printStackTrace();
        }
    }

    @Test
    @DisplayName("test config subscription")
    public void configSubscription() {

        Namespaces namespaces = admin.namespaces();
        NamespaceName namespaceName = NamespaceName.get(tenant, "subscribes");
        HashSet<SubscriptionType> subscriptionTypes = new HashSet<>();
        subscriptionTypes.add(SubscriptionType.Failover);
        try {

            namespaces.removeAutoSubscriptionCreation(namespaceName.toString());
            namespaces.setAutoSubscriptionCreation(namespaceName.toString(),
                    AutoSubscriptionCreationOverride.builder().allowAutoSubscriptionCreation(false).build());
            namespaces.setMaxSubscriptionsPerTopic(namespaceName.toString(), 2);
            // namespaces.setMaxConsumersPerSubscription(namespaceName.toString(), 3);
            //namespaces.setSubscriptionTypesEnabled(namespaceName.toString(), subscriptionTypes);
        } catch (PulsarAdminException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("test unsubscribe")
    public void unsubscribe() {

        Namespaces namespaces = admin.namespaces();
        NamespaceName namespaceName = NamespaceName.get(tenant, "subscribes");
        String subName = "gow-role-subscription";
        try {
            namespaces.unsubscribeNamespace(namespaceName.toString(), subName);
        } catch (PulsarAdminException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("test grantPermissionOnSubscription")
    public void grantPermissionOnSubscription() {

        // 设置角色全局的订阅，可以订阅所有的topic
        Namespaces namespaces = admin.namespaces();
        NamespaceName namespaceName = NamespaceName.get(tenant, "subscribes");
        String subName = "gow-role-subscription";
        try {
            namespaces.grantPermissionOnSubscription(namespaceName.toString(), subName, Collections.singleton("gow"));
        } catch (PulsarAdminException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("test revokePermissionOnSubscription")
    public void revokePermissionOnSubscription() {


        Namespaces namespaces = admin.namespaces();
        NamespaceName namespaceName = NamespaceName.get(tenant, "subscribes");
        String subName = "gow-role-subscription";
        try {
            namespaces.revokePermissionOnSubscription(namespaceName.toString(), subName, "gow");
        } catch (PulsarAdminException e) {
            e.printStackTrace();
        }
    }

    // todo 测试策略配置

    // todo 测试授权配置


    @Test
    @DisplayName("test NamespaceName API")
    public void namespaceAPI() {
        NamespaceName namespaceName = NamespaceName.get(tenant, "namespace");
        String localName = namespaceName.toString();
        assert namespaceName.getLocalName().equals("namespace") : "error";
        assert namespaceName.getTenant().equals(tenant) : "error";
        NamespaceName namespaceName1 = NamespaceName.get(localName);
        System.out.println(namespaceName1);
    }

}
