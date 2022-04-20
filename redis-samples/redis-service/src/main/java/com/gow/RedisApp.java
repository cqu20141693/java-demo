package com.gow;

import com.alibaba.fastjson.JSONObject;
import com.gow.controller.LuaController;
import com.gow.util.DeviceRouteUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wujt
 */
@SpringBootApplication
@Slf4j
public class RedisApp implements ApplicationRunner {

    @Autowired
    private LuaController luaController;

    public static void main(String[] args) {
        SpringApplication.run(RedisApp.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String deviceKey = "device001";
        log.info("test script start");
        luaController.addRoute(deviceKey, "session0001");
        Object route = luaController.getRoute(DeviceRouteUtils.MQTT_DEVICE_KEY_PREFIX + deviceKey);
        log.info("script get route={}", JSONObject.toJSON(route));
        log.info("test script end");
    }
}
