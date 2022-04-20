package com.gow.pulsar.admin;

import com.alibaba.fastjson.JSONObject;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.admin.Topics;
import org.apache.pulsar.client.api.AuthenticationFactory;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.common.naming.NamespaceName;
import org.apache.pulsar.common.naming.TopicDomain;
import org.apache.pulsar.common.naming.TopicName;
import org.apache.pulsar.common.policies.data.AuthAction;
import org.apache.pulsar.common.policies.data.BacklogQuota;
import org.apache.pulsar.common.policies.data.RetentionPolicies;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author gow
 * @date 2021/8/25
 */
@Slf4j
public class TopicTest {

    private static final String serviceUrl = "http://172.30.203.25:8080,172.30.203.26:8080,172.30.203.24:8080";
    private static final String superToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaG9uZ2N0ZWNoLXN1cGVyLXVzZXIifQ"
                    + ".o8GzC_pMaIayEJJxwcfQsTLRuup6eL0idBFgiSJLjmo";
    private static final String adminToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjY3RlY2gifQ.TkCwKZIW-CCQEI6qjoAnEImpJzDymkYthWUymGQwArg";
    PulsarAdmin admin = PulsarAdmin.builder()
            .authentication(AuthenticationFactory.token(superToken))
            .serviceHttpUrl(serviceUrl)
            .build();
    PulsarAdmin adminRole = PulsarAdmin.builder()
            .authentication(AuthenticationFactory.token(adminToken))
            .serviceHttpUrl(serviceUrl)
            .build();

    public TopicTest() throws PulsarClientException {
    }

    private final static String tenant = "cloud";
    private final static String namespace = "subscribes";
    private final static String subscribeRole = "gow";
    private final static String topicName =
            TopicName.get(TopicDomain.persistent.value(), tenant, namespace, "test-partitioned-topic").toString();

    @Test
    @DisplayName("test getTopic")
    public void getNamespaceInfo() {

        Topics topics = admin.topics();
        NamespaceName namespaceName = NamespaceName.get(tenant, namespace);
        try {
            List<String> list = topics.getList(namespaceName.toString());
            log.info("namespace={} has topics={}", namespaceName.toString(), JSONObject.toJSONString(list));
        } catch (PulsarAdminException pulsarAdminException) {
            pulsarAdminException.printStackTrace();
        }

    }

    @Test
    @DisplayName("test createPartitionedTopic")
    public void createPartitionedTopic() {

        Topics topics = admin.topics();
        NamespaceName namespaceName = NamespaceName.get(tenant, namespace);
        try {
            TopicName topicName =
                    TopicName.get(TopicDomain.persistent.value(), namespaceName, "test-partitioned-topic");
            topics.createPartitionedTopic(topicName.toString(), 1);
        } catch (PulsarAdminException pulsarAdminException) {
            pulsarAdminException.printStackTrace();
        }
    }

    @Test
    @DisplayName("test createMissedPartitions")
    public void createMissedPartitions() {

        Topics topics = admin.topics();
        NamespaceName namespaceName = NamespaceName.get(tenant, namespace);
        try {
            TopicName topicName = TopicName.get(TopicDomain.persistent.value(), namespaceName, "test-missed-topic");
            topics.createMissedPartitions(topicName.toString());
        } catch (PulsarAdminException pulsarAdminException) {
            pulsarAdminException.printStackTrace();
        }
    }

    @Test
    @DisplayName("test createNonPartitionedTopic")
    public void createNonPartitionedTopic() {

        Topics topics = admin.topics();
        NamespaceName namespaceName = NamespaceName.get(tenant, namespace);
        try {
            TopicName topicName = TopicName.get(TopicDomain.non_persistent.value(), namespaceName, "test-non-topic");
            topics.createNonPartitionedTopic(topicName.toString());
        } catch (PulsarAdminException pulsarAdminException) {
            pulsarAdminException.printStackTrace();
        }
    }

    @Test
    @DisplayName("test deletePartitionedTopic")
    public void deletePartitionedTopic() {

        Topics topics = admin.topics();
        NamespaceName namespaceName = NamespaceName.get(tenant, namespace);
        try {
            TopicName topicName = TopicName.get(TopicDomain.persistent.value(), namespaceName, "test-topic");
            topics.deletePartitionedTopic(topicName.toString());
        } catch (PulsarAdminException pulsarAdminException) {
            pulsarAdminException.printStackTrace();
        }
    }

    @Test
    @DisplayName("test deleteTopic")
    public void deleteTopic() {

        Topics topics = admin.topics();
        NamespaceName namespaceName = NamespaceName.get(tenant, namespace);
        try {
            TopicName topicName = TopicName.get(TopicDomain.non_persistent.value(), namespaceName, "test-topic");
            topics.delete(topicName.toString(), false, true);
        } catch (PulsarAdminException pulsarAdminException) {
            pulsarAdminException.printStackTrace();
        }
    }

    @Test
    @DisplayName("test getPermissions")
    public void getPermissions() {

        Topics topics = admin.topics();
        NamespaceName namespaceName = NamespaceName.get(tenant, namespace);
        try {
            TopicName topicName =
                    TopicName.get(TopicDomain.persistent.value(), namespaceName, "test-partitioned-topic");
            Map<String, Set<AuthAction>> permissions = topics.getPermissions(topicName.toString());
            log.info("topic={} has permissions={}", topicName.toString(), JSONObject.toJSONString(permissions));
        } catch (PulsarAdminException pulsarAdminException) {
            pulsarAdminException.printStackTrace();
        }
    }

    @Test
    @DisplayName("test grantPermission")
    public void grantPermission() {
        Topics topics = admin.topics();
        HashSet<AuthAction> authActions = new HashSet<>();
        authActions.add(AuthAction.produce);
        authActions.add(AuthAction.consume);
        try {
            topics.grantPermission(topicName, subscribeRole, authActions);
        } catch (PulsarAdminException pulsarAdminException) {
            pulsarAdminException.printStackTrace();
        }
    }

    @Test
    @DisplayName("test revokePermissions")
    public void revokePermissions() {
        Topics topics = admin.topics();
        try {
            topics.revokePermissions(topicName, subscribeRole);
        } catch (PulsarAdminException pulsarAdminException) {
            pulsarAdminException.printStackTrace();
        }
    }


    @Test
    @DisplayName("test terminateTopic")
    public void terminateTopic() {
        Topics topics = admin.topics();
        TopicName terminate = TopicName.get(TopicDomain.persistent.value(), "cloud", "subscribes", "terminate");
        try {
            topics.terminateTopic(terminate.toString());
        } catch (PulsarAdminException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("test deleteSubscription")
    public void deleteSubscription() {

        String subName = "admin-role-subscription";
        log.info("deleteSubscription topic={},subName={}", topicName, subName);
        Topics topics = admin.topics();
        try {
            topics.deleteSubscription(topicName, subName, true);
        } catch (PulsarAdminException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("test createSubscription")
    public void createSubscription() {
        Topics topics = adminRole.topics();

        try {
            String subName = "gow-role-subscription";
            topics.createSubscription(topicName, subName, MessageId.latest);
            topics.setMaxConsumers(topicName, 2);
            RetentionPolicies policies = new RetentionPolicies(24 * 60, 100);
            topics.setRetention(topicName, policies);
            BacklogQuota backlogQuota = BacklogQuota.builder().limitSize(100 * 1024 * 1024).limitTime(12 * 3600)
                    .retentionPolicy(BacklogQuota.RetentionPolicy.consumer_backlog_eviction).build();
            topics.setBacklogQuota(topicName, backlogQuota);
            topics.setMessageTTL(topicName, 12 * 3600);
        } catch (PulsarAdminException e) {
            e.printStackTrace();
        }
    }
    @Test
    @DisplayName("test setResourceConstraint")
    public void setResourceConstraint() {
        Topics topics = adminRole.topics();

        // 设置单个topic的限制
        try {
            topics.setMaxConsumers(topicName, 2);
            RetentionPolicies policies = new RetentionPolicies(24 * 60, 100);
            topics.setRetention(topicName, policies);
            BacklogQuota backlogQuota = BacklogQuota.builder().limitSize(100 * 1024 * 1024).limitTime(12 * 3600)
                    .retentionPolicy(BacklogQuota.RetentionPolicy.consumer_backlog_eviction).build();
            topics.setBacklogQuota(topicName, backlogQuota);
            topics.setMessageTTL(topicName, 12 * 3600);
        } catch (PulsarAdminException e) {
            e.printStackTrace();
        }
    }

}
