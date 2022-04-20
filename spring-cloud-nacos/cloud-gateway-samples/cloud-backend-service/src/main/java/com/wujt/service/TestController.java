package com.wujt.service;

import com.gow.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gow
 * @date 2021/7/18 0018
 */
@RestController
@RequestMapping("api/test")
public class TestController {

    @GetMapping("greet")
    Result<String> greet(@RequestParam("name") String name) {
        return Result.ok("hello " + name);
    }
}
