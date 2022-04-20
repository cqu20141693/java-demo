package com.wujt;


import cmo.gow.dubbo.spi.uitls.DubboServiceLoader;
import com.gow.spi.core.ExtensionLoader;
import com.gow.spring.spi.util.SpringServiceLoader;
import com.wujt.config.SPIConfig;
import com.wujt.spi.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;
import java.util.ServiceLoader;

/**
 * @author wujt
 */
@Slf4j
@SpringBootApplication(scanBasePackages = {"com.gow", "com.wujt"})
public class SpiApp implements CommandLineRunner {

    @Autowired
    private SPIConfig spiConfig;

    public static void main(String[] args) {

        SpringApplication.run(SpiApp.class, args);
    }

    @Override
    public void run(String... args) {
        getInstanceByJavaSpi();

        getInstanceBySpring();


        getInstanceByDubbo(Protocol.class);
        getRegistryByDubbo(Registry.class);

        getInstanceByGow(DatabasePool.class);
    }

    private void getRegistryByDubbo(Class<Registry> registryClass) {

        URL url = URL.valueOf("http://localhost:8080/test")
                .addParameter("service", "helloService");
        getExtension(url);

        URL addParameter = url.addParameter("registry", "etcd");
        getExtension(addParameter);

    }

    private Registry getExtension(URL url) {
        org.apache.dubbo.common.extension.ExtensionLoader<Registry> extensionLoader = DubboServiceLoader.getExtensionLoader(Registry.class);
        Registry registry = extensionLoader
                .getAdaptiveExtension();
        String register = registry.register(url, "maple");
        log.info("register info={}", register);
        return registry;
    }

    private void getInstanceByJavaSpi() {
        ServiceLoader<Developer> shouts = ServiceLoader.load(Developer.class);
        for (Developer s : shouts) {
            s.introduction();
        }
        log.info("jdk spi iml start ,config={}", spiConfig);

        ServiceLoader<MessageProducer> producers = ServiceLoader.load(MessageProducer.class);
        for (MessageProducer producer : producers) {
            spiConfig.getAdapters().forEach(producer::send);

        }
    }

    private void getInstanceBySpring() {
        log.info("test spring spi start ");
        Developer instances = SpringServiceLoader.getSpringFactoriesInstances(Developer.class);
        instances.introduction();
        MessageProducer messageProducer = SpringServiceLoader.getSpringFactoriesInstances(MessageProducer.class);
        messageProducer.send("rabbit mq");
        log.info("test spring spi end ");

    }

    private void getInstanceByGow(Class<DatabasePool> type) {
        log.info("test gow spi start ");
        DatabasePool mongo = ExtensionLoader.getExtensionLoader(type).getDefaultJoin();
        Optional.ofNullable(mongo).ifPresent(databasePool -> {
            databasePool.getConnection();
            databasePool.execute("db.user.findOne{\"name\":\"gow\"} ");
            databasePool.close();
        });

        DatabasePool mysql = ExtensionLoader.getExtensionLoader(type).getJoin("mysql");
        Optional.ofNullable(mysql).ifPresent(databasePool -> {
            databasePool.getConnection();
            databasePool.execute("select * from user u ");
            databasePool.close();
        });

        DatabasePool redis = ExtensionLoader.getExtensionLoader(type).getJoin("redis");
        Optional.ofNullable(redis).ifPresent(databasePool -> {
            databasePool.getConnection();
            databasePool.execute("flushdb");
            databasePool.close();
        });

        log.info("test gow spi end ");
    }

    private void getInstanceByDubbo(Class<Protocol> type) {
        log.info("test dubbo spi start");
        Protocol instance = DubboServiceLoader.getDubboSPIInstance(type);
        instance.getDefaultPort();
        instance.export(null);
        instance.refer(null, null);
        instance.destroy();
        log.info("test dubbo spi end");
    }
}
