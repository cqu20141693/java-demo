package com.cc.core.service;

import com.alibaba.fastjson.JSONObject;
import com.cc.core.index.DeviceGeo;
import com.cc.starter.controller.domain.vo.DeviceGeoReq;
import com.cc.starter.controller.domain.vo.DeviceGeoUpdateReq;
import org.elasticsearch.common.unit.DistanceUnit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.ByQueryResponse;
import org.springframework.data.geo.Point;

import java.util.List;

public interface DeviceService {


    DeviceGeo save(DeviceGeoReq req);

    ByQueryResponse updateNameByDeviceId(String deviceId, String name);

    String updateNameById(String id, String name);

    Object geoPointSearch(JSONObject geoJson);

    List<DeviceGeo> searchByNameAndDeviceIdAndTag(String name, String deviceId, String tag, Pageable pageable);

    SearchHits<DeviceGeo> geoSearchByDistance(Point point, Double distance, DistanceUnit unit, Integer page, Integer pageSize);

    List<DeviceGeo> searchByDeviceId(String deviceId);

    /**
     * 当id对应的文档不存在时，新建文档
     * 当id对应的文档存在时，更新文档
     *
     * @param req
     * @return
     */
    String upsert(DeviceGeoUpdateReq req);

    Object geoShapeSearch(JSONObject geoJson);
}


