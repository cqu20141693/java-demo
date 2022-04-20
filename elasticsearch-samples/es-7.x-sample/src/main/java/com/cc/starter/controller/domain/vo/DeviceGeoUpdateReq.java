package com.cc.starter.controller.domain.vo;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.cc.core.index.DeviceGeo;
import lombok.Data;
import org.springframework.data.elasticsearch.core.convert.GeoConverters;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.geo.Point;

import java.util.Date;
import java.util.List;

@Data
public class DeviceGeoUpdateReq {
    /**
     * 文档id
     */
    @JSONField(serialize = false)
    private String id;
    /**
     * 根据设备id精确搜索
     */
    @JSONField()
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
    private Point location;

    /**
     * geo 图形
     */
    private JSONObject shape;

    /**
     * 标签精确匹配搜索
     */
    private List<String> tags;

    public DeviceGeo toDeviceGeo() {
        DeviceGeo deviceGeo = new DeviceGeo();
        deviceGeo.setDeviceId(deviceId);
        deviceGeo.setName(name);
        deviceGeo.setCreateTime(createTime);
        deviceGeo.setLocation(location);
        deviceGeo.setTags(tags);
        deviceGeo.setShape(GeoConverters.MapToGeoJsonConverter.INSTANCE.convert(shape));
        return deviceGeo;
    }
}
