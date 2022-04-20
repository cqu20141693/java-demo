package com.gow.codec.base;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class SignTest {
    @Test
    public void md5Test(){

            String s = DigestUtils.md5Hex("qwrqr".getBytes(StandardCharsets.UTF_8));
            System.out.println(s);
            String hex = DigestUtils.md5Hex(new byte[0]);
            System.out.println(hex);
    }
}
