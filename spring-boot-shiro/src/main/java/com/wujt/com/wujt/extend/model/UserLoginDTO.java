package com.wujt.com.wujt.extend.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author wujt
 */
@Data
public class UserLoginDTO {
    @NotBlank(message = "用户名或手机号不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "(?!.*[\\u4E00-\\u9FA5\\s])(?!^[a-zA-Z]+$)(?!^[\\d]+$)(?!^[^a-zA-Z\\d]+$)^.{6,20}$", message = "密码格式错误，密码长度6-20位，必须包含字母、数字和特殊符号中至少两种")
    private String password;
    private String ip;
    private String codeHashKey;
    private String verifyCode;
}
