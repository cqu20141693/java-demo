package com.cc.things.metadata;

/**
 * 格式化支持接口
 * <p>
 * wcc 2022/6/4
 */
public interface FormatSupport {
    /**
     * 对值进行格式化
     *
     * @param value 值
     * @return 格式化后的值
     */
    Object format(Object value);
}
