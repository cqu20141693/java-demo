package com.gow.codec.bytes.serializable;

/**
 * 序列化器
 * wcc 2022/5/5
 */
public interface Serializer {
    /**
     * 序列化对象
     *
     * @param obj
     * @return
     */
    byte[] serialize(Object obj);

}
