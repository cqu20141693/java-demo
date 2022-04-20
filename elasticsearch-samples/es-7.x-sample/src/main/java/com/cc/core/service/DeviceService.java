package com.cc.core.service;

import com.alibaba.fastjson.JSONObject;
import com.cc.core.index.DeviceGeo;
import com.cc.starter.controller.domain.vo.DeviceGeoReq;
import org.elasticsearch.common.unit.DistanceUnit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.ByQueryResponse;
import org.springframework.data.geo.Point;

import java.util.List;
import java.util.Map;

public interface DeviceService {


    DeviceGeo save(DeviceGeoReq req);

    ByQueryResponse updateNameByDeviceId(String deviceId, String name);

    String updateNameById(String id, String name);

    Object geoSearch(JSONObject geoJson);

    SearchHits<DeviceGeo> geoSearchByDistance(Point point, Double distance, DistanceUnit unit, Integer page, Integer pageSize);

    List<DeviceGeo> searchByDeviceId(String deviceId);

}


