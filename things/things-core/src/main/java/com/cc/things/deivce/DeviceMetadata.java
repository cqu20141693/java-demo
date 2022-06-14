package com.cc.things.deivce;

import com.cc.things.ThingMetadata;
import com.cc.things.metadata.EventMetadata;
import com.cc.things.metadata.FunctionMetadata;
import com.cc.things.metadata.MergeOption;
import com.cc.things.metadata.PropertyMetadata;

import java.util.List;

/**
 * 设备物模型定义
 * wcc 2022/6/4
 */
public interface DeviceMetadata extends ThingMetadata {

    /**
     * @return 所有属性定义
     */
    List<PropertyMetadata> getProperties();

    /**
     * @return 所有功能定义
     */
    List<FunctionMetadata> getFunctions();

    /**
     * @return 事件定义
     */
    List<EventMetadata> getEvents();

    /**
     * @return 标签定义
     */
    List<PropertyMetadata> getTags();

    /**
     * 合并物模型，合并后返回新的物模型对象
     *
     * @param metadata 要合并的物模型
     * @since 1.1.6
     */
    @Override
    default <T extends ThingMetadata> DeviceMetadata merge(T metadata) {
        return this.merge(metadata, MergeOption.DEFAULT_OPTIONS);
    }

    default <T extends ThingMetadata> DeviceMetadata merge(T metadata, MergeOption... options) {
        throw new UnsupportedOperationException("unsupported merge metadata");
    }
}
