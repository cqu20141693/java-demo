package com.gow.pulsar.test.admin;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.admin.ResourceGroups;
import org.apache.pulsar.client.admin.ResourceQuotas;
import org.apache.pulsar.common.policies.data.ResourceQuota;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/7
 */
@Component
@Slf4j
public class ResourceGroupAPI implements ApplicationRunner {

    private PulsarAdmin pulsarAdmin;

    public ResourceGroupAPI(PulsarAdmin pulsarAdmin) {
        this.pulsarAdmin = pulsarAdmin;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        ResourceGroups resourcegroups = pulsarAdmin.resourcegroups();
        ResourceQuotas resourceQuotas = pulsarAdmin.resourceQuotas();

        ResourceOperation(resourceQuotas);
    }

    private void ResourceOperation(ResourceQuotas resourceQuotas) throws PulsarAdminException {
        ResourceQuota defaultResourceQuota = resourceQuotas.getDefaultResourceQuota();
        log.info("default ResourceQuota={}", JSONObject.toJSONString(defaultResourceQuota));
    }
}
