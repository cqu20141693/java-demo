package com.wujt.mongo.version4.model;

import lombok.Data;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/3/13
 */
@Data
public class ThingsModel {

    private String name;
    private String identifier;
    private Integer type;
    private Model data;

    public ThingsModel getTestDat() {
        this.name = "测试";
        this.identifier = "test";
        this.type = 0;
        this.data = new Model();
        return this;
    }
}
