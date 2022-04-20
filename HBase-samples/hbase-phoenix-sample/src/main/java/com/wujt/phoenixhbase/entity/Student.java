package com.wujt.phoenixhbase.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author luolh
 * @version 1.0
 * @since 2020/8/24 18:12
 */
@Data
public class Student {
    private Integer id;
    private String name;
    private Integer age;
    private String gender;
    private Double weight;
    private Date createTime;
}
