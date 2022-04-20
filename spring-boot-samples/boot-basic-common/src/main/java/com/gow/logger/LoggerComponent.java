package com.gow.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wujt  2021/5/21
 */
public class LoggerComponent {
    private static Logger logger = LoggerFactory.getLogger(LoggerComponent.class);


    public static void info(String message) {
        logger.info(message);
    }

    public static void info(String message, Object o1, Object o2) {
        logger.info(message, o1, o2);
    }

    public static void info(String message, Object... objects) {
        logger.info(message, objects);
    }

    public static void info(String message, Throwable throwable) {
        logger.info(message, throwable);
    }

    public static void info(String message, Object object) {
        logger.info(message, object);
    }

    public static void debug(String message) {
        logger.debug(message);
    }

    public static void warn(String message) {
        logger.warn(message);
    }

    public static void trace(String message) {
        logger.trace(message);
    }
    public static void error(String message) {
        logger.error(message);
    }
}
