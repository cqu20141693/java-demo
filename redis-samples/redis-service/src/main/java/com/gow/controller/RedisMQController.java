package com.gow.controller;

import com.gow.redis.operation.RedisMQFacade;
import com.gow.redis.utils.RedisClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wujt  2021/5/14
 */
@RestController
@RequestMapping("redis/mq")
public class RedisMQController {

    @Autowired
    private RedisMQFacade redisMQFacade;

    @Autowired
    private RedisClientUtil redisClientUtil;

    @GetMapping("pub")
    public String pubMessage(@RequestParam("topic") String topic, @RequestParam("value") String value) {
        redisMQFacade.publish(topic, value);
        return "success";
    }

    @GetMapping("sub")
    public String subTopic(@RequestParam("topic") String topic, @RequestParam("channel") Boolean channel) {
        if (channel) {
            redisMQFacade.addChannelTopic(topic);
        } else {
            redisMQFacade.addPatternTopic(topic);
        }
        return "success";
    }

    @GetMapping("unsub")
    public String unsubTopic(@RequestParam("topic") String topic, @RequestParam("channel") Boolean channel) {
        if (channel) {
            redisMQFacade.removeChannelTopic(topic);
        } else {
            redisMQFacade.removePatternTopic(topic);
        }
        return "success";
    }

    @GetMapping("leftPop")
    public Map<String, String> leftPop() {
        String key1 = "/broker/bus/iiot-platform:8848/iiot-platform";
        String key2 = "/broker/bus/iiot-platform:8840/iiot-platform";

        String leftPop = redisClientUtil.leftPop(key1, "8840");
        String leftPop1 = redisClientUtil.leftPop(key2, "8848");

        HashMap<String, String> map = new HashMap<>();
        map.put(key1, leftPop);
        map.put(key2, leftPop1);
        return map;
    }
}
