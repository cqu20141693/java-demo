package com.cc.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * wcc 2022/6/6
 */
public class EnumDict {


    /**
     * 获取枚举通过掩码
     *
     * @param tClass
     * @param mask
     * @param <T>
     * @return
     */
    public static <T extends Enum<?>> List<T> getByMask(Class<T> tClass, long mask) {

        return getByMask(Arrays.asList(tClass.getEnumConstants()), mask);
    }

    /**
     * 利用枚举的自自然序将1左移
     *
     * @param allOptions
     * @param mask
     * @param <T>
     * @return
     */
    static <T extends Enum<?>> List<T> getByMask(List<T> allOptions, long mask) {
        if (allOptions.size() >= 64) {
            throw new UnsupportedOperationException("不支持选项超过64个数据字典!");
        }
        List<T> arr = new ArrayList<>();
        List<T> all = allOptions;
        for (T t : all) {
            if (((1L << t.ordinal()) & mask) != 0) {
                arr.add(t);
            }
        }
        return arr;
    }

    @SafeVarargs
    public static <T extends Enum<?>> long toMask(T... t) {
        if (t == null) {
            return 0L;
        }
        long value = 0L;
        for (T t1 : t) {
            value |= getMask(t1);
        }
        return value;
    }

    private static <T extends Enum<?>> long getMask(T t1) {
        return 1L << t1.ordinal();
    }

}
