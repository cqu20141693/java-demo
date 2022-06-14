package com.cc.things.metadata.types;

import com.cc.things.metadata.FormatSupport;
import com.cc.things.metadata.Metadata;

import java.util.Map;

/**
 * 物模型数据类型
 *
 * wcc 2022/6/4
 */
public interface DataType extends Metadata,Validator, FormatSupport {

    /**
     * @return 类型标识
     */
    default String getType() {
        return getId();
    }

    /**
     * @return 拓展属性
     */
    @Override
    default Map<String, Object> getExpands() {
        return null;
    }

}
