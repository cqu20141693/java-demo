package com.gow.oss.model;

import com.gow.common.ReturnCode;

/**
 * @author wujt  2021/5/24
 */
public enum OSSCode implements ReturnCode {

    /**
     * 图片格式错误
     *
     * @date 2021/5/24 16:31
     */
    PICTURE_FORMAT_ERROR("010000001", "picture format error"),
    PICTURE_NULL_ERROR("0100000002", "picture stream null error"),
    ;
    /**
     * 业务编号
     */
    private String code;

    /**
     * 业务信息
     */
    private String message;

    OSSCode(String code, String message) {
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
