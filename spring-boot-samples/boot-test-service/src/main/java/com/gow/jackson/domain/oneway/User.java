package com.gow.jackson.domain.oneway;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author gow
 * @date 2021/7/24
 */
public class User {
    private String name;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer identifier;

    private User() {
    }

    public User(String name, String password, Integer identifier) {
        this.name = name;
        this.password = password;
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public Integer getIdentifier() {
        return identifier;
    }

    private void setIdentifier(Integer identifier) {
        this.identifier = identifier;
    }
}