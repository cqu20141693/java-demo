package com.gow.redis.domain;

/**
 * @author gow
 * @date 2021/6/24
 */
public enum CacheDomainEnum {

    USER_CACHE("u");

    // 前缀
    private String prefix;

    CacheDomainEnum(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }


}
