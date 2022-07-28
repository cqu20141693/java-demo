package com.gow.spring.session;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * wcc 2022/7/27
 */
@Data
public class SessionHolder {
    private static ThreadLocal<SessionContext> SESSION_CONTEXT = ThreadLocal.withInitial(() -> {
        SessionContext sessionContext = new SessionContext();
        sessionContext.setApplicationId(1L);
        sessionContext.setCallTime(LocalDateTime.now());
        sessionContext.setUser(new SessionContext.UserContext(1L, "admin", "超级管理员"));
        sessionContext.setTenant(new SessionContext.TenantContext(1L, "wcc"));
        return sessionContext;
    });


    public static SessionContext getContext() {
        return SESSION_CONTEXT.get();
    }

    public static void putContext(SessionContext context) {
        SESSION_CONTEXT.set(context);
    }

    public static void clearContext() {
        SESSION_CONTEXT.remove();
    }

    public static Long getUserId() {
        return SessionHolder.getContext().getUser().getUserId();
    }

}
