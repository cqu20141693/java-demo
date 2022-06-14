package com.cc.sip.exception;

import lombok.Data;

import javax.sip.message.Response;

/**
 * sip 请求异常
 * wcc 2022/5/24
 */
@Data
public class SipRequestException extends RuntimeException {
    private final String reason;

    private final Response response;

    public SipRequestException(String reason, Response response) {
        super(reason);
        this.reason = reason;
        this.response = response;

    }
}
