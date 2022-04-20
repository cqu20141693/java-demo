package cmo.gow.dubbo.spi.impl;

import com.wujt.spi.Registry;
import org.apache.dubbo.common.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gow
 * @date 2021/6/27 0027
 */
public class ZookeeperRegistry implements Registry {
    private Logger logger = LoggerFactory.getLogger(ZookeeperRegistry.class);

    @Override
    public String register(URL url, String content) {
        logger.info("服务: {} 已注册到zookeeper上，备注: {}", url.getParameter("service"), content);

        return "Zookeeper register already! ";
    }

    @Override
    public String discovery(URL url, String content) {
        logger.info("zookeeper上发现服务: {} , 备注: {}", url.getParameter("service"), content);

        return "Zookeeper discovery already! ";
    }
}
