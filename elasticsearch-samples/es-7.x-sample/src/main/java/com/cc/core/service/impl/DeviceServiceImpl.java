package com.cc.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cc.core.index.DeviceGeo;
import com.cc.core.repository.DeviceGeoRepository;
import com.cc.core.service.DeviceService;
import com.cc.starter.controller.domain.vo.DeviceGeoReq;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.RestStatusException;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ScriptType;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.convert.GeoConverters;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService {

    public static final String DEVICE_INDEX = "device_geo";

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Autowired
    private DeviceGeoRepository deviceGeoRepository;

    @Override
    public DeviceGeo save(DeviceGeoReq req) {
        DeviceGeo deviceGeo = new DeviceGeo();
        deviceGeo.setDeviceId(req.getDeviceId());
        deviceGeo.setName(req.getName());
        deviceGeo.setCreateTime(req.getCreateTime());
        deviceGeo.setLocation(new Point(req.getLocation().getLat(), req.getLocation().getLon()));
        deviceGeo.setTags(req.getTags());
        deviceGeo.setShape(GeoConverters.MapToGeoJsonConverter.INSTANCE.convert(req.getShape()));
        return deviceGeoRepository.save(deviceGeo);
    }

    // update

    public ByQueryResponse updateNameByDeviceId(String deviceId, String name) {
        Criteria criteria = new Criteria("deviceId").is(deviceId);
        Query query = new CriteriaQuery(criteria);
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("deviceId", deviceId);
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", name);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
        UpdateQuery.Builder builder = UpdateQuery.builder(query)
                .withScriptType(ScriptType.INLINE)
                .withParams(params).withScript("ctx._source.name = params.name");
        // .withAbortOnVersionConflict(true);
        return elasticsearchTemplate.updateByQuery(builder.build(), IndexCoordinates.of(DEVICE_INDEX));
    }

    public String updateNameById(String id, String name) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", name);
        UpdateQuery updateQuery = UpdateQuery.builder(id).withParams(params)
                .withScript("ctx._source.name = params.name").build();
        IndexCoordinates indexCoordinates = IndexCoordinates.of(DEVICE_INDEX);
        try {
            UpdateResponse response = elasticsearchTemplate.update(updateQuery, indexCoordinates);
            return response.getResult().name();
        } catch (RestStatusException statusException) {
            if (statusException.getStatus() == 404) {
                log.info("occur 404 ,msg={}", statusException.getMessage());
            }
        }
        return "exception";
    }

    @Override
    public Object geoSearch(JSONObject geoJson) {

        return null;
    }

    public SearchHits<DeviceGeo> geoSearchByDistance(Point point, Double distance, DistanceUnit unit, Integer page, Integer pageSize) {
        // 指定坐标的字段location，需要是地理坐标类型的
        GeoDistanceQueryBuilder distanceQueryBuilder = new GeoDistanceQueryBuilder("location")
                .point(point.getX(), point.getY())//经纬度坐标
                .distance(distance, unit);  // 范围，第二个参数是单位
        //也可以使用ElasticsearchTemplate
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withFilter(distanceQueryBuilder).build();
        return elasticsearchTemplate.search(searchQuery, DeviceGeo.class, IndexCoordinates.of(DEVICE_INDEX));
    }

    @Override
    public List<DeviceGeo> searchByDeviceId(String deviceId) {
        Criteria criteria = new Criteria("deviceId").is(deviceId);
        Query query = new CriteriaQuery(criteria);
        SearchHits<DeviceGeo> searchHits = elasticsearchTemplate.search(query, DeviceGeo.class, IndexCoordinates.of(DEVICE_INDEX));
        return searchHits.stream().map(searchHit -> {
                    // doc id
                    String id = searchHit.getId();
                    //
                    List<Object> sortValues = searchHit.getSortValues();
                    return searchHit.getContent();
                }
        ).collect(Collectors.toList());
    }
}
