package com.wujt.com.wujt.extend.model;

/**
 *
 * @author: wjt
 */
public enum StatusEnum
{
    /**
     * 启用
     */
    ENABLE(10),
    /**
     * 禁用
     */
    DISABLE(-10);

    private Integer value;

    StatusEnum(Integer value) {
        this.value = value;
    }

    public Integer intValue() {
        return value;
    }

    public Integer getValue() {
        return value;
    }
}
