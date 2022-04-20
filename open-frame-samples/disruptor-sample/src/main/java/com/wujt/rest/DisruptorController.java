package com.wujt.rest;

import com.wujt.disruptor.DisruptorProducer;
import com.wujt.disruptor.sample.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wujt  2021/5/8
 */
@RestController
@RequestMapping("disruptor")
public class DisruptorController {

    @Autowired
    private DisruptorProducer<Log> disruptorProducer;
    private AtomicInteger count = new AtomicInteger();

    @PostMapping("add")
    public String add(@RequestParam("value") String value) {
        Log log = new Log(count.incrementAndGet(), value);
        disruptorProducer.send(log);
        return "success";
    }
}
