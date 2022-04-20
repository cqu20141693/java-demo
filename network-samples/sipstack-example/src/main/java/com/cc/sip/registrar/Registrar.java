package com.cc.sip.registrar;

import com.cc.sip.SimpleSipStack;

/**
 * @author gow
 * @date 2022/2/15
 */
public final class Registrar {


    public static void main(final String[] args) throws Exception {
        final RegistrarHandler handler = new RegistrarHandler();
        final SimpleSipStack stack = new SimpleSipStack(handler, "127.0.0.1", 5060);
        stack.run();
    }
}
