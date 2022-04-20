package com.wujt.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author wujt
 */
@Component
public class HostUtil {
    private static final Logger logger = LoggerFactory.getLogger(HostUtil.class);
    public static Integer port;

    public static String restIp;

    @Value("${server.port}")
    public void setPort(Integer port) {
        HostUtil.port = port;
        logger.info("restIp={},port={}", HostUtil.restIp, HostUtil.port);
    }

    static {
        try {
            restIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("get the ip address error!", e);
        }
        //    logger.info("restIp={},port={}", HostUtil.restIp, HostUtil.port);
    }


    @Autowired
    private Environment environment;

    /**
     * 当配置文件中配置了server.address，则返回server.address，否则返回本机IP
     *
     * @return the rest ip
     */
    public String getRestIp() {
        String restIp = environment.getProperty("server.address");
        if (StringUtils.isEmpty(restIp)) {
            try {
                restIp = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                logger.error("get the ip address error!", e);
            }
        }

        return restIp;
    }

    public int getRestPort() {
        return Integer.parseInt(environment.getProperty("server.port"));
    }
}
