package com.cc.sip.proxyregister;

import com.cc.sip.SimpleSipStack;

/**
 * @author gow
 * @date 2022/2/15
 */
public class ProxyRegistrar {
    public static void main(final String[] args) throws Exception {
        final ProxyRegistrarHandler handler = new ProxyRegistrarHandler();
        final SimpleSipStack stack = new SimpleSipStack(handler, "10.0.1.28", 5060);
        handler.setStack(stack);
        stack.run();
    }

}
