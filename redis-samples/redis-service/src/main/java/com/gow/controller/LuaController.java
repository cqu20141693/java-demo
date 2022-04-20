package com.gow.controller;

import com.gow.domain.RedisDeviceInfo;
import com.gow.util.DeviceRouteUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

@RestController
@RequestMapping("lua")
@Slf4j
public class LuaController {

    @Autowired
    private DeviceRouteUtils deviceRouteUtils;

    @GetMapping("addRoute")
    public Object addRoute(@RequestParam("deviceKey") String deviceKey, @RequestParam("sessionKey") String sessionKey) throws UnknownHostException {
        RedisDeviceInfo redisDeviceInfo = new RedisDeviceInfo();
        redisDeviceInfo.setIp(InetAddress.getLocalHost().getHostAddress());
        redisDeviceInfo.setPort(1883);
        redisDeviceInfo.setSession(sessionKey);
        redisDeviceInfo.setGateway(true);
        RedisDeviceInfo redisDeviceInfo1 = deviceRouteUtils.addRouteKey(deviceKey, redisDeviceInfo, 30 );
        HashMap<String, Object> map = new HashMap<>();
        if (redisDeviceInfo1 == null) {
            map.put("add", true);
            log.info("add success");
        } else {
            map.put("add", false);
            map.put("old", redisDeviceInfo1.toRedisValue());
            log.info("add failed,should remove old and sync disconnect,then add new");
        }
        return map;
    }

    @GetMapping("refreshRoute")
    public Object refreshRoute(@RequestParam("deviceKey") String deviceKey, @RequestParam("sessionKey") String sessionKey) throws UnknownHostException {
        RedisDeviceInfo redisDeviceInfo = new RedisDeviceInfo();
        redisDeviceInfo.setIp(InetAddress.getLocalHost().getHostAddress());
        redisDeviceInfo.setPort(1883);
        redisDeviceInfo.setSession(sessionKey);
        redisDeviceInfo.setGateway(true);
        int result = deviceRouteUtils.refreshRouteKey(deviceKey, redisDeviceInfo, 30);
        HashMap<String, Object> map = new HashMap<>();
        if (result == -1) {
            log.error("there are more than 1 client with the same deviceKey {} connected. this situation should not exists, this client will be close.info={} ", deviceKey, redisDeviceInfo);
            map.put("refresh", result);
        } else {
            map.put("refresh", result);
        }
        return map;
    }

    @GetMapping("removeRoute")
    public Object removeRoute(@RequestParam("deviceKey") String deviceKey, @RequestParam("sessionKey") String sessionKey) throws UnknownHostException {
        RedisDeviceInfo redisDeviceInfo = new RedisDeviceInfo();
        redisDeviceInfo.setIp(InetAddress.getLocalHost().getHostAddress());
        redisDeviceInfo.setPort(1883);
        redisDeviceInfo.setSession(sessionKey);
        redisDeviceInfo.setGateway(true);
        Boolean result = deviceRouteUtils.removeRouteKey(deviceKey, redisDeviceInfo);
        HashMap<String, Object> map = new HashMap<>();
        if (!result) {
            log.error("remove routeKey failed,maybe info not match");
            map.put("remove", result);
        } else {
            map.put("remove", result);
        }
        return map;
    }

    @GetMapping("getRoute")
    public RedisDeviceInfo getRoute(@RequestParam("deviceKey") String deviceKey) {
        return deviceRouteUtils.getDeviceRouteInfo(deviceKey);
    }

    @GetMapping("getRouteTTL")
    public Object getRouteTTL(@RequestParam("deviceKey") String deviceKey) {
        return deviceRouteUtils.getRouteTTL(deviceKey);
    }
}
