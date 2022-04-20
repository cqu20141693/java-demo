package com.cc.starter.controller.domain.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.Date;
import java.util.List;

@Data
public class DeviceGeoReq {

    /**
     * 根据设备id精确搜索
     */
    private String deviceId;

    /**
     * 根据设备名称模糊搜索，利用分词
     */
    private String name;

    /**
     * 根据创建时间进行聚合统计查询
     */
    private Date createTime;

    /**
     * 根据地理位置进行搜索查询
     */
    private GeoPoint location;

    /**
     * geo 图形
     */
    private JSONObject shape;

    /**
     * 标签精确匹配搜索
     */
    private List<String> tags;


}
