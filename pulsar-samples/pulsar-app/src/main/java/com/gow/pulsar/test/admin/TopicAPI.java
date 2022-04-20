package com.gow.pulsar.test.admin;

import com.alibaba.fastjson.JSONObject;
import com.gow.pulsar.core.domain.PulsarTenantUtil;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.Lookup;
import org.apache.pulsar.client.admin.Namespaces;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.admin.Topics;
import org.apache.pulsar.common.policies.data.AuthAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/7
 */
@Component
@Slf4j
public class TopicAPI implements ApplicationRunner {

    @Autowired
    private PulsarTenantUtil pulsarTenantUtil;

    private PulsarAdmin pulsarAdmin;

    public TopicAPI(PulsarAdmin pulsarAdmin) {
        this.pulsarAdmin = pulsarAdmin;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        Topics topics = pulsarAdmin.topics();
        Lookup lookups = pulsarAdmin.lookups();
        Namespaces namespaces = pulsarAdmin.namespaces();

        topicOperation(topics, namespaces);
        lookupsOperation(lookups);
    }

    private void lookupsOperation(Lookup lookups) throws PulsarAdminException {
        String topicName = pulsarTenantUtil.getDefaultTopic(true, true);
        String lookupTopic = lookups.lookupTopic(topicName);
        String hello = lookups.lookupTopic("hello");
        log.info("lookupTopic ={} ,hello={}", lookupTopic, hello);
    }

    private void topicOperation(Topics topics, Namespaces namespaces) throws PulsarAdminException {
        log.info("admin topic api test start");
        String topicName = pulsarTenantUtil.getDefaultTopic(true, true);
        String singleTopicName = pulsarTenantUtil.getDefaultTopic(true, false);
        String nonTopicName = pulsarTenantUtil.getDefaultTopic(false, false);
        // Try to create partitions for partitioned topic. The partitions of partition topic has to be created, can
        // be used by repair partitions when topic auto creation is disabled
        List<String> topicsList = namespaces.getTopics(pulsarTenantUtil.getNamespace());
        log.info("topic-test topics={}", topicsList);
        // Create a partitioned topic.

//        if ( topicExist(topicsList, topicName)) {
//            log.info("topic-test topic name={} exist", topicName);
//        } else {
//            topics.createPartitionedTopic(topicName, 2);
//            log.info("topic-test createPartitionedTopic name={},num={}", topicName, 2);
//        }
//        if ( topicExist(topicsList, nonTopicName)) {
//            log.info("topic-test topic nonTopicName={} exist", nonTopicName);
//        } else {
//            topics.createNonPartitionedTopic(nonTopicName);
//            log.info("topic-test createNonPartitionedTopic name={}", nonTopicName);
//        }
//        if (topicExist(topicsList, singleTopicName)) {
//            log.info("topic-test topic singleTopicName={} exist", singleTopicName);
//        } else {
//            topics.createNonPartitionedTopic(singleTopicName);
//            log.info("topic-test createMissedPartitions singleTopicName={}", singleTopicName);
//        }
        //topics.delete(topicName);
        // Delete a partitioned topic.
        //topics.deletePartitionedTopic(topicName);


        Set<AuthAction> actions = new HashSet<>();
        Collections.addAll(actions, AuthAction.values());
        topics.grantPermission(topicName, pulsarTenantUtil.getRole(), actions);
        Map<String, Set<AuthAction>> permissions = topics.getPermissions(topicName);
        log.info("topic-test grantPermission topicName={},role={},actions={},permission={}", topicName,
                pulsarTenantUtil.getRole(),
                JSONObject.toJSONString(actions), JSONObject.toJSONString(permissions));

    }

}
