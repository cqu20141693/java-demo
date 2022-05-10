package com.cc.flowable.API.vo;

import lombok.Data;

/**
 * 请假请求
 * wcc 2022/5/10
 */
@Data
public class HolidayReq {
    private String employee;
    // 请假天数
    private Integer holidays;
    private String reason;
}
