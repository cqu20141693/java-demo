package com.gow.rest;

import com.gow.redis.operation.RedisStringFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wujt  2021/5/10
 */
@RestController
@RequestMapping("redis")
public class RedisController {


    @Autowired
    private RedisStringFacade redisStringFacade;


    @GetMapping("get")
    public String getMessage(@RequestParam("key") String key) {
        return redisStringFacade.get(key);
    }

    @GetMapping("set")
    public String setMessage(@RequestParam("key") String key, @RequestParam("value") String value) {
        redisStringFacade.set(key, value);
        return "success";
    }

}
