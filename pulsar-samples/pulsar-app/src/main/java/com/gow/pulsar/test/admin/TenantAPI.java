package com.gow.pulsar.test.admin;

import com.alibaba.fastjson.JSONObject;
import com.gow.pulsar.core.domain.PulsarProperties;
import com.gow.pulsar.core.domain.PulsarTenantUtil;
import java.util.HashSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.admin.Tenants;
import org.apache.pulsar.common.policies.data.TenantInfo;
import org.apache.pulsar.common.policies.data.TenantInfoImpl;
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
public class TenantAPI implements ApplicationRunner {

    @Autowired
    private PulsarTenantUtil pulsarTenantUtil;

    @Autowired
    private PulsarProperties pulsarProperties;
    private PulsarAdmin pulsarAdmin;

    public TenantAPI(PulsarAdmin pulsarAdmin) {
        this.pulsarAdmin = pulsarAdmin;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        Tenants tenants = pulsarAdmin.tenants();

        tenantOperation(tenants);
    }

    private void tenantOperation(Tenants tenants) throws PulsarAdminException {
        HashSet<String> roles = new HashSet<>();
        roles.add(pulsarTenantUtil.getRole());
        HashSet<String> clusters = new HashSet<>();
        clusters.add(pulsarProperties.getCluster());

        String tenant = pulsarTenantUtil.getName();
        try {
            TenantInfo info = tenants.getTenantInfo(tenant);
            if (info != null) {
                log.info("tenant-test: exist tenant={} info={}", tenant, JSONObject.toJSONString(info));
            }
        } catch (PulsarAdminException pulsarAdminException) {
            TenantInfo tenantInfo = TenantInfoImpl.builder().adminRoles(roles)
                    .allowedClusters(clusters)
                    .build();
            tenants.createTenant(pulsarTenantUtil.getName(), tenantInfo);
            TenantInfo info = tenants.getTenantInfo(pulsarTenantUtil.getName());
            log.info("tenant-test: add tenant={},info={}", tenant, JSONObject.toJSONString(info));
        }

        // tenants.updateTenant(tenant, info);

    }
}
