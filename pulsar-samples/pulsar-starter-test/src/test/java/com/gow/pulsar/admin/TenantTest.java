package com.gow.pulsar.admin;

import com.alibaba.fastjson.JSONObject;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.admin.Tenants;
import org.apache.pulsar.client.api.AuthenticationFactory;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.common.policies.data.TenantInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author gow
 * @date 2021/8/25
 */
@Slf4j
public class TenantTest {

//    private static final String serviceUrl = "http://localhost:8080";

    private static final String webServiceUrl = "http://172.30.203.25:8080,172.30.203.26:8080,172.30.203.24:8080";

    private static final String cluster = "pulsar-cluster";
    //private static final String cluster="pulsar-local-cluster";

    private static final String superToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaG9uZ2N0ZWNoLXN1cGVyLXVzZXIifQ"
                    + ".o8GzC_pMaIayEJJxwcfQsTLRuup6eL0idBFgiSJLjmo";
    PulsarAdmin admin;

    public TenantTest() throws PulsarClientException {
        admin = PulsarAdmin.builder()
                .authentication(AuthenticationFactory.token(superToken))
                .serviceHttpUrl(webServiceUrl)
                .build();
    }

    public TenantTest(String webServiceUrl) throws PulsarClientException {
        admin = PulsarAdmin.builder()
                .authentication(AuthenticationFactory.token(superToken))
                .serviceHttpUrl(webServiceUrl)
                .build();
    }

    @Test
    @DisplayName("test getTenantInfo")
    public void getTenantInfo() {
        Tenants tenants = admin.tenants();
        try {

            List<String> list = tenants.getTenants();
            log.info("tenants={}", list);
            String tenantName = "cloud";
            if (list.contains(tenantName)) {
                TenantInfo tenantInfo = tenants.getTenantInfo(tenantName);
                log.info("cloud tenant exist policies={}", JSONObject.toJSONString(tenantInfo));
            } else {
                log.info("cloud tenant not exist");
            }
        } catch (PulsarAdminException.NotFoundException e) {
            log.info("NotFoundException={}", e.getMessage());
        } catch (PulsarAdminException pulsarAdminException) {
            pulsarAdminException.printStackTrace();
        }
    }

    /**
     * 可进行集群和adminRole 授权
     */
    @Test
    @DisplayName("test createTenant")
    public void createTenant() {
        String tenantName = "cloud";
        createTenant(tenantName);
    }

    public void createTenant(String tenantName) {
        Tenants tenants = admin.tenants();
        try {

            List<String> list = tenants.getTenants();
            log.info("tenants={}", list);

            if (list.contains(tenantName)) {
                log.info("cloud tenant exist");
            } else {
                log.info("cloud tenant not exist");

                TenantInfo tenantInfo =
                        TenantInfo.builder().adminRoles(Collections.singleton("cc-tech")).allowedClusters(
                                Collections.singleton(cluster)).build();
                tenants.createTenant(tenantName, tenantInfo);
            }
        } catch (PulsarAdminException pulsarAdminException) {
            pulsarAdminException.printStackTrace();
        }
    }

    /**
     * 可进行集群和adminRole 授权
     */
    @Test
    @DisplayName("test updateTenant")
    public void updateTenant() {
        Tenants tenants = admin.tenants();
        try {

            List<String> list = tenants.getTenants();
            log.info("tenants={}", list);
            String tenantName = "cloud";
            if (list.contains(tenantName)) {

                TenantInfo tenantInfo = tenants.getTenantInfo(tenantName);
                log.info("cloud tenant exist policies={}", JSONObject.toJSONString(tenantInfo));
                Set<String> adminRoles = tenantInfo.getAdminRoles();
                String adminRole = "gow";
                adminRoles.add(adminRole);
                tenants.updateTenant(tenantName, tenantInfo);
            } else {
                log.info("cloud tenant not exist");

            }
        } catch (PulsarAdminException pulsarAdminException) {
            pulsarAdminException.printStackTrace();
        }
    }


    @Test
    @DisplayName("test deleteTenant")
    public void deleteTenant() {
        Tenants tenants = admin.tenants();
        try {

            List<String> list = tenants.getTenants();
            log.info("tenants={}", list);
            String tenantName = "cloud";
            if (list.contains(tenantName)) {

                TenantInfo tenantInfo = tenants.getTenantInfo(tenantName);
                log.info("cloud tenant exist policies={}", JSONObject.toJSONString(tenantInfo));
                tenants.deleteTenant(tenantName);
            } else {
                log.info("cloud tenant not exist");
            }
        } catch (PulsarAdminException pulsarAdminException) {
            pulsarAdminException.printStackTrace();
        }
    }
}
