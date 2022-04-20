package com.gow.pulsar.test.admin;

import com.alibaba.fastjson.JSONObject;
import com.gow.pulsar.core.domain.PulsarProperties;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.Bookies;
import org.apache.pulsar.client.admin.BrokerStats;
import org.apache.pulsar.client.admin.Brokers;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.common.policies.data.BookiesClusterInfo;
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
public class BrokerAPI implements ApplicationRunner {

    @Autowired
    private PulsarProperties properties;
    private PulsarAdmin pulsarAdmin;

    public BrokerAPI(PulsarAdmin pulsarAdmin) {
        this.pulsarAdmin = pulsarAdmin;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        Bookies bookies = pulsarAdmin.bookies();
        Brokers brokers = pulsarAdmin.brokers();
        BrokerStats brokerStats = pulsarAdmin.brokerStats();

        brokerOperation(brokers);
        bookiesOperation(bookies);
        brokerStatsOperation(brokerStats);
    }

    private void brokerStatsOperation(BrokerStats brokerStats) throws PulsarAdminException {
        log.info("admin brokerStats api test start");
//        LoadManagerReport loadReport = brokerStats.getLoadReport();
//        log.info(
//                "brokerStats-test: loadReport cpu={},memory={},bandWidthIn={},bandWidthOut={},msgRateIn={},"
//                        + "msgThroughputOut={},numConsumer={},numProducer={} ",
//                loadReport.getCpu(), loadReport.getMemory(),
//                loadReport.getBandwidthIn(), loadReport.getBandwidthOut(), loadReport.getMsgRateIn(),
//                loadReport.getMsgThroughputOut(), loadReport.getNumConsumers(), loadReport.getNumProducers());
        String topics = brokerStats.getTopics();
        String metrics = brokerStats.getMetrics();
        String mBeans = brokerStats.getMBeans();
        log.info("brokerStats-test: topics={},metrics={},mBeans={}", topics, metrics, mBeans);
        log.info("admin brokerStats api test end");
    }

    private void bookiesOperation(Bookies bookies) throws PulsarAdminException {
        log.info("admin bookie api test start");
        BookiesClusterInfo bookiesBookies = bookies.getBookies();
        log.info("bookie: bookie={}", JSONObject.toJSONString(bookiesBookies));
        log.info("admin bookie api test end");
    }

    private void brokerOperation(Brokers brokers) throws PulsarAdminException {
        log.info("admin broker api test start");
        String cluster = properties.getCluster();
        List<String> activeBrokers = brokers.getActiveBrokers(cluster);
        log.info("broker-test:activeBrokers={},", activeBrokers);
//        BrokerInfo leaderBroker = brokers.getLeaderBroker();
//        log.info("broker-test:leaderUrl={}", leaderBroker.getServiceUrl());
//        BrokersImpl brokersImpl = (BrokersImpl) brokers;
//        String adminPath="admin/v2/brokers";
//        String ownedNamespacePath=adminPath+"/"+"ownedNamespaces";
//        Map<String, NamespaceOwnershipStatus> ownedNamespaces = brokers.getOwnedNamespaces(cluster, null);
//        log.info("broker-test:ownedNamespaces={},", JSONObject.toJSONString(ownedNamespaces));
        Map<String, String> allDynamicConfigurations = brokers.getAllDynamicConfigurations();
        Map<String, String> runtimeConfigurations = brokers.getRuntimeConfigurations();
        log.info("broker-test:allDynamicConfigurations={},runtimeConfigurations={}",
                JSONObject.toJSONString(allDynamicConfigurations), JSONObject.toJSONString(runtimeConfigurations));
        log.info("broker-test:admin broker api test end");
    }
}


