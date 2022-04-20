package cmo.gow.dubbo.spi.impl;

import com.wujt.spi.Registry;
import org.apache.dubbo.common.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gow
 * @date 2021/6/27 0027
 */
public class EtcdRegistry implements Registry {

    private Logger logger = LoggerFactory.getLogger(EtcdRegistry.class);

    @Override
    public String register(URL url, String content) {
        logger.info("服务: {} 已注册到 Etcd 上，备注: {}", url.getParameter("service"), content);

        return "Etcd register already! ";
    }

    @Override
    public String discovery(URL url, String content) {
        logger.info("Etcd 上发现服务: {} , 备注: {}", url.getParameter("service"), content);

        return "Etcd discovery already! ";
    }
}