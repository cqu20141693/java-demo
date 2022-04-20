package com.gow.controller;

import com.wujt.producer.MsgProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wujt  2021/4/29
 */
@RestController
@RequestMapping("kafka")
public class KafkaController {

    @Autowired
    private MsgProducer msgProducer;

    @PostMapping("push")
    public void push(@RequestParam("key") String key, @RequestParam("value") String value) {
        msgProducer.send(key, value);
    }
}
