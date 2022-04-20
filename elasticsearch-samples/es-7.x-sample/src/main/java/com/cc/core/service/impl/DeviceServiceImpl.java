package com.cc.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cc.core.index.DeviceGeo;
import com.cc.core.repository.DeviceGeoRepository;
import com.cc.core.service.DeviceService;
import com.cc.starter.controller.domain.vo.DeviceGeoReq;
import com.cc.starter.controller.domain.vo.DeviceGeoUpdateReq;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.geometry.Rectangle;
import org.elasticsearch.index.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.RestStatusException;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.convert.GeoConverters;
import org.springframework.data.elasticsearch.core.document.Document;
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

    @Autowired
    private ElasticsearchConverter elasticsearchConverter;

    @Override
    public DeviceGeo save(DeviceGeoReq req) {
        // 经度longitude 东西（180，-180）->x， 纬度latitude 南北（0，90）->y
        DeviceGeo deviceGeo = new DeviceGeo();
        deviceGeo.setDeviceId(req.getDeviceId());
        deviceGeo.setName(req.getName());
        deviceGeo.setCreateTime(req.getCreateTime());
        deviceGeo.setLocation(new Point(req.getLocation().getLon(), req.getLocation().getLat()));
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

    /**
     * @param geoJson
     * @return
     */
    @SneakyThrows
    @Override
    public Object geoPointSearch(JSONObject geoJson) {
        HashMap<String, Object> map = new HashMap<>();
        // 构建分页
        Pageable page = PageRequest.of(0, 999);
        double maxY = 39.928739;
        double minX = 116.398634;
        GeoPoint top = new GeoPoint(maxY, minX);
        double minY = 39.920162;
        double maxX = 116.407904;
        GeoPoint bottom = new GeoPoint(minY, maxX);
        NativeSearchQuery query = getGeoBoxQuery(top, bottom);
        GeoPoint center = new GeoPoint(39.939783, 116.379285);
        SearchHits<DeviceGeo> box = elasticsearchTemplate.search(query, DeviceGeo.class, IndexCoordinates.of(DEVICE_INDEX));
        map.put("box", box);
        Double distance = 2.5;
        DistanceUnit unit = DistanceUnit.KILOMETERS;
        NativeSearchQuery geoDistanceQuery = getGeoDistanceQuery(center, distance, unit);
        SearchHits<DeviceGeo> distanceSearch = elasticsearchTemplate.search(geoDistanceQuery, DeviceGeo.class, IndexCoordinates.of(DEVICE_INDEX));
        map.put("distance", distanceSearch);

        GeoShapeQueryBuilder shape = QueryBuilders.geoShapeQuery("location", new Rectangle(minX, maxX, maxY, minY)).relation(ShapeRelation.WITHIN);
        NativeSearchQuery shapeQuery = new NativeSearchQueryBuilder().withQuery(shape).build();
        SearchHits<DeviceGeo> shapeSearch = elasticsearchTemplate.search(shapeQuery, DeviceGeo.class, IndexCoordinates.of(DEVICE_INDEX));
        map.put("shape", shapeSearch);
        return map;
    }

    private NativeSearchQuery getGeoDistanceQuery(GeoPoint center, Double distance, DistanceUnit unit) {
        GeoDistanceQueryBuilder distanceQueryBuilder = QueryBuilders.geoDistanceQuery("location").point(center).distance(distance, unit);
        return new NativeSearchQueryBuilder().withQuery(distanceQueryBuilder).build();
    }

    private NativeSearchQuery getGeoBoxQuery(GeoPoint top, GeoPoint bottom) {
        GeoBoundingBoxQueryBuilder boxQuery = QueryBuilders.geoBoundingBoxQuery("location").setCorners(top, bottom);
        return new NativeSearchQueryBuilder().withQuery(boxQuery).build();
    }

    /**
     * must : 跟AND 一样的
     * should : 跟OR 一样
     * matchQuery，指定字段模糊查询，跟mysql中的like类似，minimumShouldMatch可以用在match查询中，设置最少匹配了多少百分比的能查询出来
     *
     * @param name     : queryStringQuery  查询解析查询字符串(搜索范围是全部字段)
     * @param deviceId : termQuery : 完全匹配一个词语
     * @param tag      :matchQuery，指定字段模糊查询
     * @param pageable :分页
     * @return
     */
    public List<DeviceGeo> searchByNameAndDeviceIdAndTag(String name, String deviceId, String tag, Pageable pageable) {
        Pageable page = PageRequest.of(0, 999);
        QueryStringQueryBuilder stringQuery = QueryBuilders.queryStringQuery(name)
                .minimumShouldMatch("75%"); // 最小的匹配度
        MatchQueryBuilder match = QueryBuilders.matchQuery("tag", tag);
        TermQueryBuilder termQuery = QueryBuilders.termQuery("deviceId", deviceId);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(stringQuery).must(termQuery).should(match);
        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder).withPageable(pageable).build();
        SearchHits<DeviceGeo> search = elasticsearchTemplate.search(query, DeviceGeo.class, IndexCoordinates.of(DEVICE_INDEX));
        return search.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    private List<DeviceGeo> multiGetByDeviceId(String deviceId) {
        CriteriaQuery query = new CriteriaQuery(Criteria.where("deviceId").is(deviceId));
        List<MultiGetItem<DeviceGeo>> items = elasticsearchTemplate.multiGet(query, DeviceGeo.class, IndexCoordinates.of(DEVICE_INDEX));
        return items.stream().map(MultiGetItem::getItem).collect(Collectors.toList());
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

    @Override
    public String upsert(DeviceGeoUpdateReq req) {

        Document document = elasticsearchConverter.mapObject(req.toDeviceGeo());
        UpdateQuery updateQuery =
                UpdateQuery.builder(req.getId())
                        .withDocAsUpsert(true)
                        .withDocument(document)
                        .withScriptType(ScriptType.INLINE)
                        .build();
        return elasticsearchTemplate.update(updateQuery, IndexCoordinates.of(DEVICE_INDEX)).getResult().name();
    }

    @Override
    public Object geoShapeSearch(JSONObject geoJson) {
        return null;
    }
}
