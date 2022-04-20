package com.wujt.model;

import com.alibaba.fastjson.JSONObject;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SendCmdRequest {


    private String domain;

    private JSONObject context;

    @NotBlank(message = "groupKey不能为空")
    @Length(min = 16, max = 16, message = "groupKey长度为16字节")
    private String groupKey;

    @NotBlank(message = "sn不能为空")
    private String sn;

    @Length(max = 64, message = "category长度为64字节")
    private String category;

    @NotBlank(message = "type不能为空")
    private String type;

    @NotNull(message = "下行消息内容不能为空")
    private Object content;

    @Min(value = 1, message = "ackWaitTime must be more than 0")
    @Max(value = 30, message = "ackWaitTime must be less than 31")
    private int ackWaitTime = 5;

    @Min(value = 1, message = "execWaitTime must be more than 0")
    @Max(value = 1800, message = "ackWaitTime must be less than 1801")
    private int execWaitTime = 300;

    @Min(value = 1L, message = "delay Time must be more than 0")
    private Integer delayTime;

    private Boolean wakeNotify = false;
}
