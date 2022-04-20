package com.wujt.asserts;

/**
 * @author gow
 * @date 2021/7/9
 */
public class AssertTest {

    public static void main(String[] args) {
        String name = "gow";
        assertNotNull(name);
        assertNull(null);


    }

    private static void assertNull(Object o) {
        assert o==null : "is null";
    }

    private static void assertNotNull(String name) {
        assert name != null : "is not null";
    }
}
