package com.gow.common;

/**
 * @author wujt  2021/5/24
 */
public enum CommonCode implements ReturnCode {

    /**
     * 请求成功
     */
    success("200", null),
    /**
     * 未知错误
     */
    UNKNOWN_ERROR("0000000000", "unknown error"),

    /**
     * 缺少要求的http body
     */
    MISSING_REQUIRED_HTTP_BODY("0000000001", "missing required http body"),

    /**
     * 参数类型不匹配
     */
    PARAMETER_TYPE_MISMATCH("0000000002", "parameter type mismatch"),

    /**
     * 参数校验失败
     */
    VALIDATION_ERROR("0000000003", "parameter verification failed"),

    /**
     * 不支持的HTTP方法
     */
    HTTP_METHOD_NOT_SUPPORTED("0000000004", "http method not support"),

    /**
     * 缺失Path Variable
     */
    MISSING_PATH_VARIABLE("0000000005", "miss path variable"),
    /**
     * 非法的请求
     */
    ILLEGAL_REQUEST("00000000006", "illegal request"),
    /**
     * 错误的请求
     */
    BAD_REQUEST("0000000007", "bad request"),
    /**
     * 内部错误
     */
    INTERNAL_SERVER_ERROR("0000000008", "internal server error"),
    /**
     * Feign Exception
     */
    FEIGN_EXCEPTION("0000000009", "feign exception"),
    ;

    /**
     * 业务编号
     */
    private String code;

    /**
     * 业务信息
     */
    private String message;

    CommonCode(String code, String message) {
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
