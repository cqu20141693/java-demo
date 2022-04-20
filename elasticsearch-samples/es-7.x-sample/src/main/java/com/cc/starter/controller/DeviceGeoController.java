package com.cc.starter.controller;


import com.alibaba.fastjson.JSONObject;
import com.cc.core.index.DeviceGeo;
import com.cc.core.repository.DeviceGeoRepository;
import com.cc.core.service.DeviceService;
import com.cc.starter.controller.domain.vo.DeviceGeoReq;
import com.cc.starter.controller.domain.vo.DeviceGeoUpdateReq;
import com.cc.starter.controller.domain.vo.GeoDistanceReq;
import com.gow.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.ByQueryResponse;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("device")
public class DeviceGeoController {

    @Autowired
    private DeviceGeoRepository deviceGeoRepository;
    @Autowired
    private DeviceService deviceService;

    @PostMapping("/save")
    public Result<DeviceGeo> save(@RequestBody DeviceGeoReq req) {
        return Result.ok(deviceService.save(req));
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable("id") String id) {
        deviceGeoRepository.deleteById(id);
        return Result.ok("success");
    }

    @GetMapping("/findById")
    public Result<DeviceGeo> findById(@RequestParam("id") String id) {
        Optional<DeviceGeo> geoOptional = deviceGeoRepository.findById(id);
        if (geoOptional.isPresent()) {
            return Result.ok(geoOptional.get());
        } else {
            return Result.ok(null);
        }
    }

    @GetMapping("/queryByName")
    public Result<List<DeviceGeo>> queryByName(@RequestParam("name") String name) {
        List<DeviceGeo> queries = deviceGeoRepository.queryByName(name);
        List<DeviceGeo> finds = deviceGeoRepository.findByName(name);
        List<DeviceGeo> gets = deviceGeoRepository.getByName(name);

        return Result.ok(queries);
    }

    @GetMapping("/queryByDeviceId")
    public Result<List<DeviceGeo>> queryByDeviceId(@RequestParam("deviceId") String deviceId) {
        List<DeviceGeo> queries = deviceGeoRepository.queryByDeviceId(deviceId);
        List<DeviceGeo> finds = deviceGeoRepository.findByDeviceId(deviceId);
        List<DeviceGeo> gets = deviceGeoRepository.getByDeviceId(deviceId);
        List<DeviceGeo> search = deviceService.searchByDeviceId(deviceId);
        return Result.ok(queries);
    }

    @PostMapping("/updateNameById")
    public Result<String> updateNameById(@RequestParam("id") String id, @RequestParam("name") String name) {
        return Result.ok(deviceService.updateNameById(id, name));
    }

    @PostMapping("/upsert")
    public Result<String> upsert(@RequestBody DeviceGeoUpdateReq req) {
        return Result.ok(deviceService.upsert(req));
    }

    @PostMapping("/updateNameByDeviceId")
    public Result<ByQueryResponse> updateNameByDeviceId(@RequestParam("deviceId") String deviceId, @RequestParam("name") String name) {
        return Result.ok(deviceService.updateNameByDeviceId(deviceId, name));
    }

    @PostMapping("/geoSearch")
    public Object geoSearch(@RequestBody JSONObject geoJson) {
        return Result.ok(deviceService.geoPointSearch(geoJson));
    }
    @PostMapping("/geoShapeSearch")
    public Object geoShapeSearch(@RequestBody JSONObject geoJson) {
        return Result.ok(deviceService.geoShapeSearch(geoJson));
    }
    @PostMapping("/searchByNameAndDeviceIdAndTag")
    public Result<List<DeviceGeo>> searchByNameAndDeviceIdAndTag(@RequestParam("deviceId") String deviceId, @RequestParam("name") String name,
                                                                 @RequestParam("tag") String tag, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        return Result.ok(deviceService.searchByNameAndDeviceIdAndTag(name, deviceId, tag, PageRequest.of(page, size)));
    }

    @GetMapping("/geoSearchByDistance")
    public Object geoSearchByDistance(@RequestBody GeoDistanceReq req) {
        return Result.ok(deviceService.geoSearchByDistance(new Point(req.getLat(), req.getLon()), req.getDistance(),
                req.getUnit(), req.getPage(), req.getSize()));
    }
}
