package com.cc.things;

/**
 * 支持 Topic 的物类型
 * wcc 2022/6/4
 */
public interface TopicSupport {

    /**
     * 根据物类型,获取topic前缀
     *
     * @param templateId 模版ID
     * @param thingId 物ID
     * @return topic前缀
     */
    String getTopicPrefix(String templateId,String thingId);

}
