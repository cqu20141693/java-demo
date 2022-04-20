package com.gow.rest.controller;

import com.gow.common.Result;
import com.gow.spring.i18n.MessageResourceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wujt  2021/5/24
 */
@RestController
@RequestMapping("i18n")
public class I18nController {

    @Autowired
    private MessageResourceServiceImpl messageResourceServiceImpl;

    @GetMapping("default")
    public Result getDefault(@RequestParam("code") String code) {
        return Result.ok(messageResourceServiceImpl.getMessage(code));
    }

    @GetMapping("en")
    public Result getEn(@RequestParam("code") String code) {
        return Result.ok(messageResourceServiceImpl.getMessage(code));
    }

    @GetMapping("zh")
    public Result getZh(@RequestParam("code") String code) {
        return Result.ok(messageResourceServiceImpl.getMessage(code));
    }

}
