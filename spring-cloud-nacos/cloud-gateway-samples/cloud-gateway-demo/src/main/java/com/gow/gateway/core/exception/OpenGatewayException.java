package com.gow.gateway.core.exception;

/**
 * @author gow
 * @date 2021/9/10
 */
public class OpenGatewayException extends RuntimeException {
    private String code;
    /**
     * 异常相关字段(Json)
     */
    private String fields;

    public OpenGatewayException(Throwable cause) {
        super(cause);
    }

    public OpenGatewayException(String message) {
        super(message);
    }

    public OpenGatewayException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public OpenGatewayException(String code, String message) {
        super(message);
        this.code = code;
    }

    public OpenGatewayException(String code, String message, String fields) {
        super(message);
        this.code = code;
        this.fields = fields;
    }

    public OpenGatewayException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public OpenGatewayException(String code, String message, String fields, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.fields = fields;
    }

    public String getCode() {
        return code;
    }

    public String getFields() {
        return fields;
    }
}
