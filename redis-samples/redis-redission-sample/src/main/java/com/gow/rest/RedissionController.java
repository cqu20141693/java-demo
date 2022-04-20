package com.gow.rest;

import com.gow.redis.operation.RedisCommonFacade;
import com.gow.redisson.component.RedissonTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wujt  2021/5/10
 */
@RestController
@RequestMapping("redission")
public class RedissionController {


    @Autowired
    private RedissonTemplate redissionTemplate;

    @Autowired
    private RedisCommonFacade template;

    @GetMapping("getLock")
    public String getLock(@RequestParam("key") String key) {
        redissionTemplate.getLock(key);
        return "success";
    }

    @GetMapping("expire")
    public String expire(@RequestParam("key") String key, @RequestParam("time") Long time) {
        template.expire(key, time);
        return "success";
    }

    @GetMapping("del")
    public String del(@RequestParam("key") String key) {
        template.del(key);
        return "success";
    }

}
