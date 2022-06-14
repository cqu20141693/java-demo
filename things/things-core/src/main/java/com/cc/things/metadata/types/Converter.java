package com.cc.things.metadata.types;

/**
 * 转换器
 * wcc 2022/6/4
 */
public interface Converter<T> {

    T convert(Object value);
}
