package com.wujt.sms.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wujt
 */
@Data
public class SMSReqDTO {
    // 调用的应用
    @NotBlank(message = "appId can not be blank")
    private String appId;
    // 调用的对象
    @NotBlank(message = "objId can not be blank")
    private String objId;
    @NotBlank(message = "mobile can not be blank")
    private String mobile;
    @NotBlank(message = "tplCode can not be blank")
    private String tplCode;
    @NotBlank(message = "params can not be blank")
    private String params;


}
