package com.wujt.proxy.factory;

/**
 * @author wujt
 */
public interface ProxyFactory {
    <T> T getProxy(final Object target);
}
