package com.gow.spring.request;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * wcc 2022/7/27
 */
@Slf4j
public class RequestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.info("do filter, {}, {}", Thread.currentThread().getId(), request.getServletPath());
        //在ThreadLocal中添加当前线程的id
        RequestContext context = RequestHolder.getContext();
        context.setMethod(request.getMethod());
        context.setUrl(request.getRequestURI());
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
