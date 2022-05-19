package com.wujt.oop;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * git repository
 * wcc 2022/5/15
 */
@Data
@Slf4j
// private protected defult public
public class Http {
    // 类属性
    public static String DESCRIPTION = "http request";
    // 包类可以见属性
    int defaultInt;

    public String getUrl() {
        return url;
    }

    // 私有属性，类可见
    private String url;
    private String params;

    // 构造方法，创建对象时使用的方法
    public Http(String url, String params) {
//        this.url = url;
        // this
        this.params = params;
    }

    // 私有方法
    private void preHandler() {
        log.info("send pre handler");
    }

    // 共有方法
    public void send() {
        this.preHandler();
        log.info("send http request:{} {}", url, params);
        postHandler();
    }

    protected void postHandler() {
        log.info("send post handler");
    }
}
