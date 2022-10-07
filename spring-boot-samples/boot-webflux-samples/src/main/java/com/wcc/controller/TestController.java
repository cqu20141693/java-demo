package com.wcc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author wujt
 */
@RestController
@RequestMapping("/city")
public class TestController {

    @GetMapping("hello")
    public Mono<String> SayHello() {

        return Mono.just("success");
    }
}
