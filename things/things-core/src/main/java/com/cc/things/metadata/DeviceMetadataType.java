package com.cc.things.metadata;

/**
 * 设备物模型属性类型
 * wcc 2022/6/4
 */
public enum DeviceMetadataType {
    //属性
    property,
    //功能
    function,
    //功能参数
    functionParameter,
    //功能输出
    functionOutput,
    //事件
    event,
    //事件输出
    eventOutput,
    //结构体属性
    objectProperty,
    //数组元素
    arrayElement,
    //标签
    tag
}
