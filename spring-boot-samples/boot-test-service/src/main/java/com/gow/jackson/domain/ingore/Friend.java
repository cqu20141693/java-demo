package com.gow.jackson.domain.ingore;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author gow
 * @date 2021/7/24
 */

public class Friend {
    private String name;
    @JsonIgnore
    private String secret;

    private Friend() {
    }

    public Friend(String name, String secret) {
        this.name = name;
        this.secret = secret;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getSecret() {
        return secret;
    }

    private void setUuid(String secret) {
        this.secret = secret;
    }
}