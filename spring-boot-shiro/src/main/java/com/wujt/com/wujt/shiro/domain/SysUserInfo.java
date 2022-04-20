package com.wujt.com.wujt.shiro.domain;

import lombok.Data;

/**
 * @author wujt
 */
@Data
public class SysUserInfo {

    private String userKey;
    private String username;
    private String password;
    private String salt;

    public boolean valid() {
        return true;
    }
}
