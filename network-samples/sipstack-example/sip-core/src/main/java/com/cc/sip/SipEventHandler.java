package com.cc.sip;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import java.util.EventObject;

/**
 * wcc 2022/6/15
 */
@Slf4j
public class SipEventHandler {

    void handleEvent(EventObject event) {
        if (event instanceof RequestEvent) {
            log.info("handle request event={}", JSONObject.toJSONString(event));
        } else if (event instanceof ResponseEvent) {
            log.info("handle response event={}", JSONObject.toJSONString(event));
        } else {
            log.info("not support event");
        }
    }

}
