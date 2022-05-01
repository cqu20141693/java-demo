package com.cc.network.cp.exception;

/**
 * 编解码异常
 * wcc 2022/4/25
 */
public class CodeCException extends RuntimeException {
    private String code;
    private String fields;

    public CodeCException(Throwable cause) {
        super(cause);
    }

    public CodeCException(String message) {
        super(message);
    }

    public CodeCException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public CodeCException(String code, String message) {
        super(message);
        this.code = code;
    }

    public CodeCException(String code, String message, String fields) {
        super(message);
        this.code = code;
        this.fields = fields;
    }

    public CodeCException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public CodeCException(String code, String message, String fields, Throwable cause) {
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
