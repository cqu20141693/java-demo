package com.gow.spring.request;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * wcc 2022/7/27
 */
@Data
@Slf4j
public class RequestContext {
    private String url;
    private String method;
    private LocalDateTime enterTime;
    private LocalDateTime completeTime;
    private JSONObject logTrance = new JSONObject();

    public void addTrace(String id, Object value) {
        Object put = logTrance.put(id, value);
    }

    public void printInfo() {
        log.info("url={} method={},enterTime={},complete={},logTrance={}", url, method, enterTime, completeTime, logTrance);
    }

    public void printWarn() {
        log.warn("url={} method={},enterTime={},complete={},logTrance={}", url, method, enterTime, completeTime, logTrance);
    }
}
