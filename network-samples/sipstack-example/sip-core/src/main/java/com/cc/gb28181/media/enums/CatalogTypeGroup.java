package com.cc.gb28181.media.enums;

/**
 * wcc 2022/5/25
 */
public enum CatalogTypeGroup {
    district("行政区划",CatalogType.district.getValue(),CatalogType.district.getValue()),
    device("设备", 111, 199),
    platform("平台", 200, 299) ,
    user("用户", 300, 499),
    platform_outer("平台外接服务器", 400, 599),
    ext("拓展类型", 600, 999),
    ;

    CatalogTypeGroup(String text, int rangeFrom, int rangeTo) {
        this.text = text;
        this.rangeFrom = rangeFrom;
        this.rangeTo = rangeTo;
    }

    private final String text;
    private final int rangeFrom;
    private final int rangeTo;
}
