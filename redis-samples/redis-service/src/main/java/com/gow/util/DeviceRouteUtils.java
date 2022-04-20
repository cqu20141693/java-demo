package com.gow.util;

import com.alibaba.fastjson.JSON;
import com.gow.domain.RedisDeviceInfo;
import com.gow.redis.utils.RedisScriptUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class DeviceRouteUtils {

    @Autowired
    private StringRedisTemplate redisTemplate;
    // D:R -> device:route
    public final static String MQTT_DEVICE_KEY_PREFIX = "D:R";
    @Autowired
    private RedisScriptUtils redisScriptUtils;

    /**
     * 心跳事件更新路由表Key
     *
     * @param routeKey
     * @param redisDeviceInfo
     * @param second          过期时间： 心跳时长延迟一分钟
     * @return int -1：失败 0&1：成功
     */
    public int refreshRouteKey(String routeKey, RedisDeviceInfo redisDeviceInfo, int second) {
        log.debug("refreshRouteKey- routeKey:{} ,redisDeviceInfo:{},second:{}", routeKey, JSON.toJSON(redisDeviceInfo), second);
        String deviceKey = MQTT_DEVICE_KEY_PREFIX + routeKey;
        String value = redisDeviceInfo.toRedisValue();
        List<String> keys = new LinkedList<>();
        keys.add(deviceKey);
        Long result;
        try {
            result = redisTemplate.execute(redisScriptUtils.getRefreshScript(), keys, value, String.valueOf(second));
            return result.intValue();
        } catch (Exception e) {
            log.error("refreshMqttKey error", e);
        }
        return -1;
    }

    /**
     * 删除路由表Key
     *
     * @param key
     */
    public void removeRouteKey(String key) {
        String deviceKey = MQTT_DEVICE_KEY_PREFIX + key;
        redisTemplate.delete(deviceKey);
    }

    /**
     * 删除路由Key
     *
     * @param routeKey
     * @param deviceInfo
     */
    public Boolean removeRouteKey(String routeKey, RedisDeviceInfo deviceInfo) {
        log.debug("removeRouteKey- routeKey:{} ,deviceInfo:{}", routeKey, deviceInfo);
        String key = MQTT_DEVICE_KEY_PREFIX + routeKey;
        String value = deviceInfo.toRedisValue();
        List<String> keys = new LinkedList<>();
        keys.add(key);
        try {
            return redisTemplate.execute(redisScriptUtils.getDisconnectScript(), keys, value);
        } catch (Exception e) {
            log.error("removeRouteKey error", e);
        }
        return false;
    }

    /**
     * auth login 时记录路由表
     *
     * @param routeKey
     * @param redisClientInfo
     * @param second
     * @return
     */
    public RedisDeviceInfo addRouteKey(String routeKey, RedisDeviceInfo redisClientInfo, int second) {
        log.debug("addRouteKey- routeKey:{} ,redisClientInfo:{},second:{}", routeKey, redisClientInfo.toRedisValue(), second);
        String key = MQTT_DEVICE_KEY_PREFIX + routeKey;
        LinkedList<String> keys = new LinkedList<>();
        keys.add(key);
        String value = redisClientInfo.toRedisValue();

        String result = redisTemplate.execute(redisScriptUtils.getRegisterScript(), keys, value, String.valueOf(second));
        if (result==null||result.isEmpty() || Objects.equals("0", result)) {
            return null;
        } else {
            RedisDeviceInfo info = new RedisDeviceInfo();
            info.fromRedisValue(result);
            return info;
        }
    }
    public RedisDeviceInfo getDeviceRouteInfo(String routeKey) {
        String value = redisTemplate.opsForValue().get(routeKey);
        return Optional.ofNullable(value).map(v -> {
            RedisDeviceInfo info = new RedisDeviceInfo();
            info.fromRedisValue(v);
            return info;
        }).orElse(null);
    }

    public Long getRouteTTL(String deviceKey) {
        return redisTemplate.getExpire(deviceKey);
    }
}
