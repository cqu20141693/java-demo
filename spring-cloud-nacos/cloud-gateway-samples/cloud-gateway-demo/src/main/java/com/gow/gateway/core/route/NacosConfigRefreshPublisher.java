package com.gow.gateway.core.route;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosPropertySourceRepository;
import com.alibaba.nacos.api.config.listener.AbstractSharedListener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wujt  2021/6/3
 * 在RouteRefreshListener 中通过心跳事件每30秒cache配置
 */
//@Component
@Slf4j
public class NacosConfigRefreshPublisher implements ApplicationContextAware, SmartLifecycle {
    private final AtomicBoolean running = new AtomicBoolean(false);
    private ApplicationContext applicationContext;

    private String routeConfigDataID = "gateway-service-route.yml";
    @Autowired
    private NacosConfigManager nacosConfigManager;

    @Override
    public void stop(Runnable callback) {

        this.stop();
        callback.run();
    }

    @Override
    public void stop() {
        if (this.running.compareAndSet(true, false)) {
        }
    }

    @Override
    public void start() {
        if (this.running.compareAndSet(false, true)) {
            NacosPropertySourceRepository.getAll().forEach(propertySource -> {
                if (!propertySource.isRefreshable()) {
                    return;
                }
                try {
                    nacosConfigManager.getConfigService().addListener(propertySource.getDataId(), propertySource.getGroup(),
                            new AbstractSharedListener() {
                                @Override
                                public void innerReceive(String dataId, String group,
                                                         String configInfo) {
                                    if (routeConfigDataID.equals(dataId)) {
                                        log.info("nacos config refresh dataId={},group={}", dataId, group);
                                        applicationContext.publishEvent(new RefreshRoutesEvent(this));
                                    }
                                }
                            });
                } catch (NacosException e) {
                    e.printStackTrace();
                }
            });
        }
    }


    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
