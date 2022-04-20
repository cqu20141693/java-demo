package com.gow.validate.pojo.common;

/**
 * @author wujt  2021/5/18
 */
public enum BusinessCode implements ReturnCode {
    /**
     * 成功错误码
     */
    success(0, "success"),
    unknown_system_error(10000, "未知系统错误"),
    missing_parameters(10001, "缺少参数"),
    parameter_type_does_not_match(10002, "参数类型不匹配"),
    file_upload_error(10003, "文件上传错误"),
    parameter_verification_failed(10004, "参数校验失败"),
    unknown_business_failure(20000, "未知业务失败"),
    resource_does_not_exist(20001, "未知业务失败"),

    ;
    /**
     * 业务编号
     */
    private int code;

    /**
     * 业务信息
     */
    private String message;

    BusinessCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
