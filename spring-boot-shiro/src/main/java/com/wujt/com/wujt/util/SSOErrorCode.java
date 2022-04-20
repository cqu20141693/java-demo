package com.wujt.com.wujt.util;

/**
 * @author wujt
 */
public enum SSOErrorCode {
    /**
     * 登录信息错误
     */
    USERNAME_PASSWORD_ERROR("0001000001", "用户名或密码错误");
    private String code;
    private String message;

    SSOErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
