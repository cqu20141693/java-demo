package com.gow.exception;

/**
 * @author wujt  2021/5/24
 */
public class CommonException extends RuntimeException {
    private String code;
    private String fields;

    public CommonException(Throwable cause) {
        super(cause);
    }

    public CommonException(String message) {
        super(message);
    }

    public CommonException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public CommonException(String code, String message) {
        super(message);
        this.code = code;
    }

    public CommonException(String code, String message, String fields) {
        super(message);
        this.code = code;
        this.fields = fields;
    }

    public CommonException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public CommonException(String code, String message, String fields, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.fields = fields;
    }

    public String getCode() {
        return this.code;
    }

    public String getFields() {
        return this.fields;
    }
}
