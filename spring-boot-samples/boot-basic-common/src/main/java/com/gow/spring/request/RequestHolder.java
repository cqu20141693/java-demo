package com.gow.spring.request;

import java.time.LocalDateTime;

/**
 * wcc 2022/7/27
 */
public class RequestHolder {

    private static ThreadLocal<RequestContext> REQUEST_CONTEXT = ThreadLocal.withInitial(() -> {
        RequestContext requestContext = new RequestContext();
        requestContext.setEnterTime(LocalDateTime.now());
        return requestContext;
    });

    public static RequestContext getContext() {
        return REQUEST_CONTEXT.get();
    }

    public static void putContext(RequestContext context) {
        REQUEST_CONTEXT.set(context);
    }

    public static void clearContext() {
        REQUEST_CONTEXT.remove();
    }

}
