package com.gow.spring.session;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * wcc 2022/7/27
 */
@Data
public class SessionContext {

    private Long applicationId;
    private LocalDateTime callTime;
    private SessionContext.UserContext user;
    private SessionContext.TenantContext tenant;

    @Data
    @AllArgsConstructor
    public static class UserContext {
        private Long userId;
        private String userName;
        private String realName;
    }

    @Data
    @AllArgsConstructor
    public static class TenantContext {
        private Long tenantId;
        private String tenantName;
    }
}
