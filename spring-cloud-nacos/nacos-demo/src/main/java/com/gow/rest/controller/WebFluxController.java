package com.gow.rest.controller;

import com.gow.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("webflux")
public class WebFluxController {

    @GetMapping("mock")
    public Result mock(){
        return Result.ok("success");
    }

}
