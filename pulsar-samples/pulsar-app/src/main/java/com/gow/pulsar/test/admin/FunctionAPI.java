package com.gow.pulsar.test.admin;

import com.gow.pulsar.core.domain.PulsarTenantUtil;
import org.apache.pulsar.client.admin.Functions;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.common.functions.FunctionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author gow
 * @date 2021/7/7
 */
@Component
public class FunctionAPI implements ApplicationRunner {

    @Autowired
    private PulsarTenantUtil pulsarTenantUtil;
    private PulsarAdmin pulsarAdmin;

    public FunctionAPI(PulsarAdmin pulsarAdmin) {
        this.pulsarAdmin = pulsarAdmin;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        Functions functions = pulsarAdmin.functions();
        functionsOperation(functions);
    }

    private void functionsOperation(Functions functions) throws PulsarAdminException {
        // createFunciton(functions, "stats-function", "link-device", "stats-func-consumer",
        //  "persistent://gow/persistent/link-device-stats", "/examples/api-examples.jar");

        // update
        // start
        // stop
        // restart
        // list
        // delete
        // get info /status
        // trigger

    }

    private void createFunciton(Functions functions, String functionName, String sourceTopicPattern,
                                String subscriptionName, String sinkTopic, String fileName)
            throws PulsarAdminException {
        FunctionConfig functionConfig = new FunctionConfig();
        functionConfig.setTenant(pulsarTenantUtil.getName());
        functionConfig.setNamespace(pulsarTenantUtil.getOriginNamespace());
        functionConfig.setName(functionName);
        functionConfig.setRuntime(FunctionConfig.Runtime.JAVA);
        functionConfig.setParallelism(1);
        functionConfig.setClassName("org.apache.pulsar.functions.api.examples.ExclamationFunction");
        functionConfig.setProcessingGuarantees(FunctionConfig.ProcessingGuarantees.ATLEAST_ONCE);
        functionConfig.setTopicsPattern(sourceTopicPattern);
        functionConfig.setSubName(subscriptionName);
        functionConfig.setAutoAck(true);
        functionConfig.setOutput(sinkTopic);
        functions.createFunction(functionConfig, fileName);
    }
}
