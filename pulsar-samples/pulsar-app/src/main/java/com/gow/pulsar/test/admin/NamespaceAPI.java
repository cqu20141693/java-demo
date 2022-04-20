package com.gow.pulsar.test.admin;

import com.alibaba.fastjson.JSONObject;
import com.gow.pulsar.core.domain.PulsarTenantUtil;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.Namespaces;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.common.policies.data.AuthAction;
import org.apache.pulsar.common.policies.data.BacklogQuota;
import org.apache.pulsar.common.policies.data.DispatchRate;
import org.apache.pulsar.common.policies.data.PersistencePolicies;
import org.apache.pulsar.common.policies.data.RetentionPolicies;
import org.apache.pulsar.common.policies.data.impl.BacklogQuotaImpl;
import org.apache.pulsar.common.policies.data.impl.DispatchRateImpl;
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
public class NamespaceAPI implements ApplicationRunner {

    @Autowired
    private PulsarTenantUtil pulsarTenantUtil;

    private PulsarAdmin pulsarAdmin;

    public NamespaceAPI(PulsarAdmin pulsarAdmin) {
        this.pulsarAdmin = pulsarAdmin;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        Namespaces namespaces = pulsarAdmin.namespaces();
        namespacesOperation(namespaces);
    }

    private void namespacesOperation(Namespaces namespaces) throws PulsarAdminException {
        String tenant = "public";
        List<String> namespacesList = namespaces.getNamespaces(tenant);
        log.info("tenant={} has namespaces={}", tenant, namespacesList);
        String defaultNamespace = "public/default";

        List<String> topics = namespaces.getTopics(defaultNamespace);
        log.info("default topic={}", JSONObject.toJSONString(topics));

        log.info("pulsar namespace api test start");
        String namespace = pulsarTenantUtil.getNamespace();

        List<String> namespaceList = namespaces.getNamespaces(pulsarTenantUtil.getName());
        if (namespaceList.contains(namespace)) {
            log.info("ns-test {} namespace exist", namespace);
        } else {
            namespaces.createNamespace(namespace);
            log.info("ns-test add {} namespace ", namespace);
        }

        RetentionPolicies retentionPolicies = new RetentionPolicies(7 * 24 * 60, 2 * 1024);

        BacklogQuota backlogQuota = new BacklogQuotaImpl(2L * 1024 * 1024 * 1024 * 8, 36000,
                BacklogQuota.RetentionPolicy.consumer_backlog_eviction);
        DispatchRate dispatchRate = new DispatchRateImpl(1000, 5 * 1024 * 1024 * 8, true, 1);
        namespaces.setBacklogQuota(namespace, backlogQuota);
        namespaces.setRetention(namespace, retentionPolicies);
        namespaces.setNamespaceMessageTTL(namespace, 30 * 24 * 3600);
        namespaces.setDeduplicationStatus(namespace, true);
        namespaces.setReplicatorDispatchRate(namespace, dispatchRate);
        PersistencePolicies persistencePolicies = new PersistencePolicies();
        namespaces.setPersistenceAsync(namespace, persistencePolicies);
        HashSet<AuthAction> authActions = new HashSet<>();
        Collections.addAll(authActions, AuthAction.values());

        namespaces.grantPermissionOnNamespace(namespace, pulsarTenantUtil.getRole(), authActions);


        log.info("pulsar namespace api test end");
    }
}
