package com.cc.gb28181.media.server.zlm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cc.bus.event.EventBus;
import com.cc.things.ThingsManager;
import com.cc.things.deivce.DeviceOperator;
import com.gow.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/media/zlm/notify")
@Slf4j
public class ZLMediaNotifyHandler {

    private final EventBus eventBus;

    private final StreamManager streamManager;

    private final ThingsManager manager;

    public ZLMediaNotifyHandler(EventBus eventBus,
                                StreamManager streamManager, ThingsManager manager) {
        this.eventBus = eventBus;
        this.streamManager = streamManager;
        this.manager = manager;
    }

    @PostMapping("/{action}")
    public ResponseEntity<Map<String, Object>> handle(@PathVariable String action, @RequestBody String jsonStr) {
        String topic = "/_sys/zlm/notify/";
        log.debug("handle zlmedia server notify [{}] : {}", action, jsonStr);

        JSONObject jsonObj = JSON.parseObject(jsonStr);
        if (jsonObj.containsKey("mediaServerId")) {
            topic = topic.concat((String) jsonObj.get("mediaServerId")).concat("/");
        }
        if ("on_play".equals(action)) {
            // 根据json返回的参数进行鉴权
            if (!(jsonObj.containsKey("stream") && jsonObj.containsKey("params"))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("code", -1));
            }
            String streamId = jsonObj.getString("stream");
            String params = jsonObj.getString("params");
            if (StringUtils.isBlank(params)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("code", -1));
            }
//                    JSONObject paramsObject = JSONObject.parseObject(params);
            if (!(params.contains("deviceId=") && params.contains("key="))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("code", -1));
            }
            String[] paramArr = params.split("&");
            if (paramArr.length != 2) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("code", -1));
            }
            String deviceId = paramArr[0].replace("deviceId=", "");
            String key = paramArr[1].replace("key=", "");
            DeviceStreamInfo info = streamManager.findStreamByStreamId(deviceId, streamId);
            if (info == null) {
                throw new CommonException("流信息不存在");
            }
            DeviceOperator device = manager.getDevice(deviceId);
            log.info("获取设备鉴权信息进行鉴权");
        }
        // 发布事件通知
        eventBus.publish(topic + action, JSON.parse(jsonStr));
        return ResponseEntity.ok(Collections.singletonMap("code", 0));

    }
}
