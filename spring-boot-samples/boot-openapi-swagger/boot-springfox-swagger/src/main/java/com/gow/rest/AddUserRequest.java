package com.gow.rest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author gow
 * @date 2021/7/12 0012
 */
@ApiModel("用户模型")
@Data
public class AddUserRequest {

    @ApiModelProperty("用户名称")
    private String username;

    @ApiModelProperty("用户手机号")
    private String tel;

    @ApiModelProperty("密码")
    private String password;
}
