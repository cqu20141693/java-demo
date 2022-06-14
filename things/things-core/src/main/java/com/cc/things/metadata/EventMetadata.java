package com.cc.things.metadata;


import com.cc.things.metadata.types.DataType;

/**
 * 事件模型
 * wcc 2022/6/4
 */
public interface EventMetadata extends Metadata {

    DataType getType();

    default EventMetadata merge(EventMetadata another, MergeOption... option){
        throw new UnsupportedOperationException("不支持事件物模型合并");
    }
}
