package com.gow.rest;

import com.gow.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gow
 * @date 2021/7/24
 */
@RestController
@RequestMapping("allow")
public class RestAllowAPI {

    @GetMapping("hello")
    public Result<String> hello() {
        return Result.ok("hello spring security");
    }
}
