package com.cc.network.cp.domian;

import org.junit.jupiter.api.Test;

class ChargePointVersionTest {

    @Test
    public void test(){
        Version version = new Version((byte) 1, (byte) 2, (byte) 3);
        byte[] encode = version.getPayload();
        System.out.println(version.getValue());
    }
}
