package com.gow.aop.aspectj.service;

import com.alibaba.fastjson.JSONObject;
import com.gow.model.ReqInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gow
 * @date 2022/1/18
 */
@Slf4j
public class LogServiceImpl implements LogService {
    @Override
    public void printLog(String userId, String reqId, ReqInfo info) {
        log.info("{} {} {} ", userId, reqId, JSONObject.toJSONString(info));
    }
}
