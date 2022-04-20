package com.gow.rest;

import com.gow.redis.operation.RedisCommonFacade;
import com.gow.redis.config.factory.GowStringRedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wujt  2021/5/10
 */
@RestController
@RequestMapping("redis/common")
public class CommonController {


    @Autowired
    private RedisCommonFacade template;

    @Autowired
    private GowStringRedisTemplate gowStringRedisTemplate;

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

    @GetMapping("set")
    public String set(@RequestParam("key") String key, @RequestParam("value") String  value) {
        gowStringRedisTemplate.opsForValue().set(key, value);
        return "success";
    }

    @GetMapping("get")
    public String get(@RequestParam("key") String key) {
       return gowStringRedisTemplate.opsForValue().get(key);
    }
}
