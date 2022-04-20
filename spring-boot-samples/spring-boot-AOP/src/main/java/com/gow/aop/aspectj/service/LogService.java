package com.gow.aop.aspectj.service;

import com.gow.model.ReqInfo;

/**
 * @author gow
 * @date 2022/1/18
 */
public interface LogService {

    void printLog(String userId, String reqId, ReqInfo info);
}
