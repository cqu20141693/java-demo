package com.cc.things.metadata;

import com.cc.things.metadata.types.DataType;

/**
 * wcc 2022/6/4
 */
public interface PropertyMetadata extends Metadata {

    DataType getValueType();

    default PropertyMetadata merge(PropertyMetadata another, MergeOption... option){
        throw new UnsupportedOperationException("不支持属性物模型合并");
    }
}
