package com.gow.common;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author wujt  2021/5/24
 */
@Data
@Accessors(chain = true)
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -504027247149928390L;

    private String code;
    private String msg;
    private String exceptionMsg;
    private T body;


    public boolean success() {
        return CommonCode.success.code().equals(code);
    }

    public static <T> Result<T> ok(T body) {
        Result<T> result = new Result<>();
        return result
                .setBody(body)
                .setCode(CommonCode.success.code())
                .setMsg(CommonCode.success.message());
    }

    public static Result<Void> ok() {
        Result<Void> result = new Result<>();
        return result
                .setCode(CommonCode.success.code())
                .setMsg(CommonCode.success.message());
    }

    public static Result<Void> fail(ReturnCode returnCode) {
        Result<Void> result = new Result<>();
        return result
                .setCode(returnCode.code())
                .setMsg(returnCode.message());
    }

    public static Result<Void> fail(ReturnCode returnCode, String exceptionMsg) {
        Result<Void> result = new Result<>();
        return result
                .setCode(returnCode.code())
                .setMsg(returnCode.message())
                .setExceptionMsg(exceptionMsg);
    }

    public static <T> Result<T> failed(ReturnCode returnCode, String exceptionMsg) {
        Result<T> result = new Result<>();
        return result
                .setCode(returnCode.code())
                .setMsg(returnCode.message())
                .setExceptionMsg(exceptionMsg);
    }
}
