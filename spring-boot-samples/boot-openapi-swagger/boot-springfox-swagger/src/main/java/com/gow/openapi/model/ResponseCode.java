package com.gow.openapi.model;

import com.gow.common.ReturnCode;

/**
 * @author gow
 * @date 2021/7/12 0012
 */
public enum ResponseCode implements ReturnCode {
    SUCCESS("success", "success"),
    FAILED("failed", "failed"),
    ;

    private String code;
    private String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
