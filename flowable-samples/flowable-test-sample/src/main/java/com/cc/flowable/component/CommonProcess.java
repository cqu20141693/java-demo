package com.cc.flowable.component;

/**
 * 公共流程
 * wcc 2022/5/10
 */
public enum CommonProcess {
    holidayRequest("holidayRequest", "Classpath", "holiday-request.bpmn20.xml"),
    ;

    CommonProcess(String id, String resourceType, String data) {
        this.id = id;
        this.resourceType = resourceType;
        this.data = data;
    }

    private String id;

    public String getId() {
        return id;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getData() {
        return data;
    }

    private String resourceType;
    private String data;
}
