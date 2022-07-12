package com.gow.crypto;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

/**
 * 签名测试
 * wcc 2022/7/5
 */
public class SignatureDemo {

    public static String generateKey(String... strings) {
        return DigestUtils.md5Hex(String.join("|", strings));
    }

    @Test
    public void testMD5() {
        // Bearer aaa
        String targetId = "197227091901341696";
        String targetType="tenant";
        String targetKey = generateKey(targetType, targetId);

        System.out.println("电厂："+targetKey);
        // Bearer aaaa
        String targetId1 = "194286499088531456";
        String targetKey1 = generateKey(targetType, targetId1);
        System.out.println("铝厂："+targetKey1);

    }
}
