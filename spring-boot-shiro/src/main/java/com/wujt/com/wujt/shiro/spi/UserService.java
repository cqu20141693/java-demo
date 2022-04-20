package com.wujt.com.wujt.shiro.spi;

import com.wujt.com.wujt.shiro.domain.SysUserInfo;

/**
 * @author wujt
 */
public interface UserService {

    SysUserInfo getByName(String username);
}
