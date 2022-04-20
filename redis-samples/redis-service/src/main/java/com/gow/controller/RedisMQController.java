package com.gow.controller;

import com.gow.redis.operation.RedisMQFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wujt  2021/5/14
 */
@RestController
@RequestMapping("redis/mq")
public class RedisMQController {

    @Autowired
    private RedisMQFacade redisMQFacade;

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
}
