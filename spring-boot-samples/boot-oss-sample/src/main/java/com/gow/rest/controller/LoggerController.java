package com.gow.rest.controller;

import com.gow.logger.LoggerComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wujt  2021/5/21
 */
@RestController
@RequestMapping("logger")
@Slf4j
public class LoggerController {


    @GetMapping("test")
    public Object info() {
        log.trace("Trace 日志...");
        log.debug("Debug 日志...");
        log.info("Info 日志...");
        log.warn("Warn 日志...");
        log.error("Error 日志...");
        return "success";
    }


    @GetMapping("component/test")
    public Object cInfo() {
        LoggerComponent.trace("Trace 日志...");
        LoggerComponent.debug("Debug 日志...");
        LoggerComponent.info("Info 日志...");
        LoggerComponent.warn("Warn 日志...");
        LoggerComponent.error("Error 日志...");
        return "component success";
    }

}
