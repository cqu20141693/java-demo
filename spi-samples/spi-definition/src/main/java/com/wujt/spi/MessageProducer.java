package com.wujt.spi;

/**
 * @author wujt
 */
public interface MessageProducer {
    <T> Boolean send(T t);
}
