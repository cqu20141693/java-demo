package com.wujt.server.mqtt.util.topic;

/**
 * @author wujt
 */
public enum SystemTopicEnum {

    ERROR("$error", "topicThe system sends error information topic");
    private String name;
    private String desc;

    SystemTopicEnum(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
