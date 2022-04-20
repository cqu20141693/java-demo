package com.gow.controller;

import org.springframework.web.bind.annotation.*;

/**
 * @author wujt
 */
@RestController
@RequestMapping("producer")
public class TestController {
    @GetMapping("test")
    public String getInfo(@RequestParam("name") String name) {
        System.out.println(name);
        return name;
    }

    @PostMapping("sayHello")
    public String sayHello(@RequestParam("name") String name) {
        System.out.println(name);
        return "hello " + name;
    }

    @GetMapping("userInfo")
    public String userInfo(@RequestParam("name") String name) {
        System.out.println(name);
        return "userInfo= " + name;
    }
}
