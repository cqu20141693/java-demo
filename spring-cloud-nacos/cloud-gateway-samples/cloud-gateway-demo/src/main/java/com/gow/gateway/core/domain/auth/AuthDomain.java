package com.gow.gateway.core.domain.auth;

/**
 * @author gow
 * @date 2021/7/17 0017
 */
public enum AuthDomain {
    user("user", "user domain auth"),
    app("app", "app domain auth"),
    device("device", "device domain auth by deviceToken"),
    group("group", "group domain auth by groupToken");

    private String id;
    private String desc;

    public String getId() {
        return id;
    }

    AuthDomain(String id, String desc) {
        this.id = id;
        this.desc = desc;
    }
}
