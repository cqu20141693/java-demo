package com.cc.things.metadata.types;

/**
 * 数据类型校验器
 * wcc 2022/6/4
 */
public interface Validator {
    /**
     * 验证是否合法
     *
     * @param value 值
     * @return ValidateResult
     */
    ValidateResult validate(Object value);
}
