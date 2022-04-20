package com.gow.test;

/**
 * @author gow
 * @date 2021/7/24
 */
public class Assert {
    private static void assertEquals(Object target, Object value) {
        if (target == null) {
            if (value != null) {
                throw new RuntimeException(" target= null not equal value={}" + value);
            }
        } else if (!target.equals(value)) {
            throw new RuntimeException(" target=" + target + "not equal value={}" + value);
        }
    }

    private static void assertTrue(boolean value) {
        assert value : "value=false is not true";
    }

    private static void assertFalse(boolean value) {
        assert !value : "value=true is not false";
    }
}
