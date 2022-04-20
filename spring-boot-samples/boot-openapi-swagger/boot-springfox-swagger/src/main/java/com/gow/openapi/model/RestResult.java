package com.gow.openapi.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author gow
 * @date 2021/7/12 0012
 */
@ApiModel("api通用返回数据")
public class RestResult<T> {

    //uuid,用作唯一标识符，供序列化和反序列化时检测是否一致
    private static final long serialVersionUID = 7498483649536881777L;
    //标识代码，0表示成功，非0表示出错
    @ApiModelProperty("标识代码,0表示成功，非0表示出错")
    private String code;

    //提示信息，通常供报错时使用
    @ApiModelProperty("提示信息,供报错时使用")
    private String msg;

    //正常返回时返回的数据
    @ApiModelProperty("返回的数据")
    private T data;

    //constructor
    public RestResult() {
    }

    //constructor
    public RestResult(String status, String msg, T data) {
        this.code = status;
        this.msg = msg;
        this.data = data;
    }

    //返回成功数据
    public RestResult success(T data) {
        return new RestResult(ResponseCode.SUCCESS.code(), ResponseCode.SUCCESS.message(), data);
    }

    public static RestResult success(String code, String msg) {
        return new RestResult(code, msg, null);
    }

    //返回出错数据
    public static RestResult error(ResponseCode code) {
        return new RestResult(code.code(), code.message(), null);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}