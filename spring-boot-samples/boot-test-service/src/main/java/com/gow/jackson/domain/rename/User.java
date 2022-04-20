package com.gow.jackson.domain.rename;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * @author gow
 * @date 2021/7/24
 */
public class User {
    private String name;
    @JsonSetter("code")
    private String externalCode;
    private String userCode;
    @JsonProperty("internalCode")
    private String code;

    private User() {
    }

    public User(String name, String externalCode, String userCode, String code) {
        this.name = name;
        this.externalCode = externalCode;
        this.userCode = userCode;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getExternalCode() {
        return externalCode;
    }

    private void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }

    @JsonGetter("publicCode")
    public String getUserCode() {
        return userCode;
    }

    @JsonSetter("userCode")
    private void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getCode() {
        return code;
    }

    private void setCode(String code) {
        this.code = code;
    }
}