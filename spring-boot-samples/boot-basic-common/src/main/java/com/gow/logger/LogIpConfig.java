package com.gow.logger;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author wujt  2021/5/21
 * Analysis and realization of configuration variables in the log
 * eg:ip
 */
public class LogIpConfig extends ClassicConverter {
    private final Logger logger = LoggerFactory.getLogger(LogIpConfig.class);

    @Override
    public String convert(ILoggingEvent event) {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException var3) {
            this.logger.error("error to obtain ip config", var3);
            return null;
        }
    }
}
