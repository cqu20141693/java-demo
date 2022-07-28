package com.gow.spring.request;

import com.gow.spring.session.SessionHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * wcc 2022/7/27
 */
@Slf4j
public class RequestCompleteInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // log.info("RequestCompleteInterceptor.afterCompletion");
        // 可以计算请求时长打印日志
        // 必须清理线程ThreadLocal 缓存
        RequestHolder.clearContext();
        SessionHolder.clearContext();
    }
}
