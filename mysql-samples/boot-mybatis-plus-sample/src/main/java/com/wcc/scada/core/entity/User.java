package com.wcc.scada.core.entity;

import lombok.Data;
import mybatis.mate.annotation.FieldEncrypt;

/**
 * 用户信息
 * wcc 2022/8/2
 */
@Data
public class User {

    private String userName;
    private String loginName;
    @FieldEncrypt
    private String password;
}
